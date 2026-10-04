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
import kotlin.coroutines.resume

class DonationBillingManager(
    context: Context,
    private val settingsRepository: SettingsRepository,
) : PurchasesUpdatedListener {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _products = MutableStateFlow<List<ProductDetails>>(emptyList())
    val products: StateFlow<List<ProductDetails>> = _products
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready
    private val _acknowledgementIssue = MutableStateFlow(false)
    val acknowledgementIssue: StateFlow<Boolean> = _acknowledgementIssue
    private val reconciliationMutex = Mutex()
    @Volatile private var isConnecting = false

    private val billingClient = BillingClient.newBuilder(appContext)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    fun connectAndRefresh() {
        if (billingClient.isReady) {
            refresh()
            return
        }
        if (isConnecting) return
        isConnecting = true
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                isConnecting = false
                _ready.value = result.responseCode == BillingClient.BillingResponseCode.OK
                if (_ready.value) refresh()
            }

            override fun onBillingServiceDisconnected() {
                isConnecting = false
                _ready.value = false
            }
        })
    }

    fun refresh() {
        if (!billingClient.isReady) return
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
            }
        }
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
        ) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                scope.launch { applyPurchases(purchases) }
            }
        }
    }

    fun purchase(activity: Activity, product: ProductDetails) {
        if (!billingClient.isReady) return
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .build()
        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(productParams)).build(),
        )
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            scope.launch { applyPurchases(purchases.orEmpty()) }
        }
    }

    private suspend fun applyPurchases(purchases: List<Purchase>) =
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

            val isDonor = ownedProducts.any { it in DonationProducts.all }
            settingsRepository.setIsPremiumUser(isDonor).first()
            publishEntitlementToWatch(isDonor)
            _acknowledgementIssue.value = acknowledgementFailed
        }

    private suspend fun acknowledgePurchase(purchase: Purchase): Boolean =
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

    private fun publishEntitlementToWatch(isDonor: Boolean) {
        val request = PutDataMapRequest.create(DonationEntitlementContract.DATA_PATH).apply {
            dataMap.putBoolean(DonationEntitlementContract.DATA_KEY, isDonor)
        }.asPutDataRequest().setUrgent()
        Wearable.getDataClient(appContext).putDataItem(request)
    }
}
