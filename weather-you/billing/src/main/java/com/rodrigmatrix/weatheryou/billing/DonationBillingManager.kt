package com.rodrigmatrix.weatheryou.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.model.DonationEntitlementContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

enum class DonationCatalogState { LOADING, AVAILABLE, UNAVAILABLE }
enum class DonationPurchaseState { IDLE, PROCESSING, PENDING, ACTIVE, FAILED }
enum class DonationRestoreState { IDLE, RESTORING, RESTORED, NO_PURCHASES, FAILED }

class DonationBillingManager(
    context: Context,
    private val settingsRepository: SettingsRepository,
) : PurchasesUpdatedListener {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _products = MutableStateFlow<List<ProductDetails>>(emptyList())
    val products: StateFlow<List<ProductDetails>> = _products
    private val _catalogState = MutableStateFlow(DonationCatalogState.LOADING)
    val catalogState: StateFlow<DonationCatalogState> = _catalogState
    private val _purchaseState = MutableStateFlow(DonationPurchaseState.IDLE)
    val purchaseState: StateFlow<DonationPurchaseState> = _purchaseState
    private val _ownedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val ownedProductIds: StateFlow<Set<String>> = _ownedProductIds
    private val _restoreState = MutableStateFlow(DonationRestoreState.IDLE)
    val restoreState: StateFlow<DonationRestoreState> = _restoreState
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready
    private val _acknowledgementIssue = MutableStateFlow(false)
    val acknowledgementIssue: StateFlow<Boolean> = _acknowledgementIssue
    private val reconciliationMutex = Mutex()
    @Volatile private var isConnecting = false
    @Volatile private var restoreRequestedAfterConnect = false

    private val billingClient = BillingClient.newBuilder(appContext)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    fun connectAndRefresh() {
        if (billingClient.isReady) {
            refresh()
            completeQueuedRestoreIfNeeded()
            return
        }
        if (isConnecting) return
        isConnecting = true
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                isConnecting = false
                _ready.value = result.responseCode == BillingClient.BillingResponseCode.OK
                if (_ready.value) {
                    refresh()
                    completeQueuedRestoreIfNeeded()
                } else {
                    _catalogState.value = DonationCatalogState.UNAVAILABLE
                    if (restoreRequestedAfterConnect) {
                        restoreRequestedAfterConnect = false
                        _restoreState.value = DonationRestoreState.FAILED
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnecting = false
                _ready.value = false
                _catalogState.value = DonationCatalogState.UNAVAILABLE
                if (restoreRequestedAfterConnect) {
                    restoreRequestedAfterConnect = false
                    _restoreState.value = DonationRestoreState.FAILED
                }
                if (_restoreState.value == DonationRestoreState.RESTORING) {
                    _restoreState.value = DonationRestoreState.FAILED
                }
                if (_purchaseState.value == DonationPurchaseState.PROCESSING) {
                    _purchaseState.value = DonationPurchaseState.FAILED
                }
            }
        })
    }

    private fun completeQueuedRestoreIfNeeded() {
        if (!restoreRequestedAfterConnect || !billingClient.isReady) return
        restoreRequestedAfterConnect = false
        _restoreState.value = DonationRestoreState.RESTORING
        refreshOwnedPurchases()
    }

    fun refresh() {
        if (!billingClient.isReady) {
            _catalogState.value = DonationCatalogState.UNAVAILABLE
            return
        }
        _catalogState.value = DonationCatalogState.LOADING
        val productParams = DonationProducts.all.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(productParams).build(),
        ) { result, details ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                _products.value = details.productDetailsList
                _catalogState.value = if (details.productDetailsList.isEmpty()) {
                    DonationCatalogState.UNAVAILABLE
                } else DonationCatalogState.AVAILABLE
            } else {
                _catalogState.value = DonationCatalogState.UNAVAILABLE
            }
        }
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
        ) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                scope.launch { applyPurchases(purchases, authoritativeSnapshot = true) }
            }
        }
    }

    fun purchase(activity: Activity, product: ProductDetails) {
        if (!billingClient.isReady) {
            _purchaseState.value = DonationPurchaseState.FAILED
            return
        }
        _purchaseState.value = DonationPurchaseState.PROCESSING
        _restoreState.value = DonationRestoreState.IDLE
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .build()
        val result = billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(productParams)).build(),
        )
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> Unit
            BillingClient.BillingResponseCode.USER_CANCELED -> _purchaseState.value = DonationPurchaseState.IDLE
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                _purchaseState.value = DonationPurchaseState.PROCESSING
                refreshOwnedPurchases()
            }
            else -> _purchaseState.value = DonationPurchaseState.FAILED
        }
    }

    fun reportPurchaseUnavailable() {
        _purchaseState.value = DonationPurchaseState.FAILED
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> scope.launch {
                val list = purchases.orEmpty()
                val hasPendingDonation = list.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PENDING &&
                        purchase.products.any { it in DonationProducts.all }
                }
                val hasPurchasedDonation = list.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        purchase.products.any { it in DonationProducts.all }
                }
                if (hasPendingDonation) {
                    _purchaseState.value = DonationPurchaseState.PENDING
                }
                val isDonor = applyPurchases(list, authoritativeSnapshot = false)
                if (hasPendingDonation) {
                    _purchaseState.value = DonationPurchaseState.PENDING
                } else if (hasPurchasedDonation && isDonor) {
                    _purchaseState.value = DonationPurchaseState.ACTIVE
                } else {
                    _purchaseState.value = DonationPurchaseState.FAILED
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseState.value = DonationPurchaseState.IDLE
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                _purchaseState.value = DonationPurchaseState.PROCESSING
                refreshOwnedPurchases()
            }
            else -> _purchaseState.value = DonationPurchaseState.FAILED
        }
    }

    fun restorePurchases() {
        _purchaseState.value = DonationPurchaseState.IDLE
        if (!billingClient.isReady) {
            _restoreState.value = DonationRestoreState.RESTORING
            restoreRequestedAfterConnect = true
            connectAndRefresh()
            return
        }
        _restoreState.value = DonationRestoreState.RESTORING
        refreshOwnedPurchases()
    }

    /** Clears local test entitlement and publishes revocation to a paired watch. */
    suspend fun resetSimulatedDonationForDebug() = reconciliationMutex.withLock {
        settingsRepository.setIsPremiumUser(false).first()
        _ownedProductIds.value = emptySet()
        _purchaseState.value = DonationPurchaseState.IDLE
        _restoreState.value = DonationRestoreState.IDLE
        _acknowledgementIssue.value = false
        publishEntitlementToWatch(false)
    }

    private fun refreshOwnedPurchases() {
        if (!billingClient.isReady) {
            if (_restoreState.value == DonationRestoreState.RESTORING) {
                _restoreState.value = DonationRestoreState.FAILED
            }
            return
        }
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
        ) { result, purchases ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                if (_restoreState.value == DonationRestoreState.RESTORING) {
                    _restoreState.value = DonationRestoreState.FAILED
                }
                if (_purchaseState.value == DonationPurchaseState.PROCESSING) {
                    _purchaseState.value = DonationPurchaseState.FAILED
                }
                return@queryPurchasesAsync
            }
            scope.launch {
                val ownedDonorPurchase = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        purchase.products.any { it in DonationProducts.all }
                }
                val pendingDonorPurchase = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PENDING &&
                        purchase.products.any { it in DonationProducts.all }
                }
                val isDonor = applyPurchases(purchases, authoritativeSnapshot = true)
                if (pendingDonorPurchase) {
                    _purchaseState.value = DonationPurchaseState.PENDING
                    _restoreState.value = DonationRestoreState.IDLE
                } else if (_restoreState.value == DonationRestoreState.RESTORING) {
                    _restoreState.value = when {
                        isDonor -> DonationRestoreState.RESTORED
                        ownedDonorPurchase || _acknowledgementIssue.value -> DonationRestoreState.FAILED
                        else -> DonationRestoreState.NO_PURCHASES
                    }
                }
                if (_purchaseState.value == DonationPurchaseState.PROCESSING) {
                    _purchaseState.value = if (isDonor) DonationPurchaseState.ACTIVE
                    else if (ownedDonorPurchase) DonationPurchaseState.FAILED
                    else DonationPurchaseState.IDLE
                }
            }
        }
    }

    private suspend fun applyPurchases(
        purchases: List<Purchase>,
        authoritativeSnapshot: Boolean,
    ): Boolean =
        reconciliationMutex.withLock {
            _acknowledgementIssue.value = false
            val ownedProducts = mutableSetOf<String>()
            var acknowledgementFailed = false

            purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                .forEach { purchase ->
                    val acknowledged = purchase.isAcknowledged || acknowledgePurchase(purchase)
                    if (acknowledged) {
                        ownedProducts.addAll(purchase.products)
                    } else {
                        acknowledgementFailed = true
                    }
                }

            val pendingDonorPurchase = purchases.any { purchase ->
                purchase.purchaseState == Purchase.PurchaseState.PENDING &&
                    purchase.products.any { it in DonationProducts.all }
            }
            val existingDonor = settingsRepository.getIsPremiumUser().first()
            val acknowledgedProducts = ownedProducts.filter { it in DonationProducts.all }.toSet()
            val nextOwnedProductIds = when {
                !authoritativeSnapshot -> _ownedProductIds.value + acknowledgedProducts
                pendingDonorPurchase -> _ownedProductIds.value + acknowledgedProducts
                else -> acknowledgedProducts
            }
            _ownedProductIds.value = nextOwnedProductIds
            val isDonor = nextOwnedProductIds.isNotEmpty() ||
                ((!authoritativeSnapshot || pendingDonorPurchase) && existingDonor)
            settingsRepository.setIsPremiumUser(isDonor).first()
            publishEntitlementToWatch(isDonor)
            _acknowledgementIssue.value = acknowledgementFailed
            isDonor
        }

    private suspend fun acknowledgePurchase(purchase: Purchase): Boolean = withTimeoutOrNull(15_000) {
        suspendCancellableCoroutine { continuation ->
            billingClient.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build(),
            ) { result ->
                if (continuation.isActive) {
                    continuation.resume(result.responseCode == BillingClient.BillingResponseCode.OK)
                }
            }
        }
    } ?: false

    private fun publishEntitlementToWatch(isDonor: Boolean) {
        val request = PutDataMapRequest.create(DonationEntitlementContract.DATA_PATH).apply {
            dataMap.putBoolean(DonationEntitlementContract.DATA_KEY, isDonor)
        }.asPutDataRequest().setUrgent()
        Wearable.getDataClient(appContext).putDataItem(request)
    }
}
