package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.ProductDetails
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.billing.DonationBillingManager
import com.rodrigmatrix.weatheryou.billing.DonationCatalogState
import com.rodrigmatrix.weatheryou.billing.DonationProducts
import com.rodrigmatrix.weatheryou.billing.DonationPurchaseState
import com.rodrigmatrix.weatheryou.billing.DonationRestoreState
import com.rodrigmatrix.weatheryou.billing.donationBillingModule
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules

private object DonationBilling : KoinComponent {
    val manager: DonationBillingManager by inject()
}

fun initializeDonationFeature() {
    loadKoinModules(donationBillingModule)
    DonationBilling.manager.connectAndRefresh()
}

@Composable
fun DonationFeatureContent(
    billingManager: DonationBillingManager = koinInject(),
    settingsRepository: SettingsRepository = koinInject(),
) {
    val context = LocalContext.current
    val products by billingManager.products.collectAsState()
    val isDonor by settingsRepository.getIsPremiumUser().collectAsState(initial = false)
    val catalogState by billingManager.catalogState.collectAsState()
    val purchaseState by billingManager.purchaseState.collectAsState()
    val ownedProductIds by billingManager.ownedProductIds.collectAsState()
    val restoreState by billingManager.restoreState.collectAsState()
    val acknowledgementIssue by billingManager.acknowledgementIssue.collectAsState()
    var showAdditionalDonations by remember { mutableStateOf(false) }
    val lifecycleOwner = context.findActivity() as? LifecycleOwner

    LaunchedEffect(billingManager) { billingManager.connectAndRefresh() }
    DisposableEffect(lifecycleOwner, billingManager) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) billingManager.connectAndRefresh()
        }
        lifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose { lifecycleOwner?.lifecycle?.removeObserver(observer) }
    }

    Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (isDonor) {
                Text(stringResource(R.string.donation_active_title))
                Text(stringResource(R.string.donation_thank_you))
                Text(stringResource(R.string.donation_active_benefits))
                if (catalogState == DonationCatalogState.AVAILABLE) {
                    val additionalProducts = products.filter { it.productId !in ownedProductIds }
                    if (showAdditionalDonations && additionalProducts.isNotEmpty()) {
                        Text(stringResource(R.string.donation_additional_note))
                        DonationOptions(
                            products = additionalProducts,
                            enabled = purchaseState != DonationPurchaseState.PROCESSING &&
                                purchaseState != DonationPurchaseState.PENDING,
                            onPurchase = { product ->
                                context.findActivity()?.let { billingManager.purchase(it, product) }
                                    ?: billingManager.reportPurchaseUnavailable()
                            },
                        )
                    } else if (additionalProducts.isNotEmpty()) {
                        TextButton(onClick = { showAdditionalDonations = true }) {
                            Text(stringResource(R.string.donation_additional_action))
                        }
                    } else {
                        Text(stringResource(R.string.donation_all_tiers_used))
                    }
                }
            } else {
                Text(stringResource(R.string.donation_title))
                Text(stringResource(R.string.donation_benefits))

                when (catalogState) {
                    DonationCatalogState.LOADING -> Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CircularProgressIndicator()
                        Text(stringResource(R.string.donation_loading))
                    }
                    DonationCatalogState.UNAVAILABLE -> {
                        Text(stringResource(R.string.donation_prices_unavailable))
                        TextButton(onClick = { billingManager.connectAndRefresh() }) {
                            Text(stringResource(R.string.donation_retry))
                        }
                    }
                    DonationCatalogState.AVAILABLE -> DonationOptions(
                        products = products.filter { it.productId !in ownedProductIds },
                        enabled = purchaseState != DonationPurchaseState.PROCESSING &&
                            purchaseState != DonationPurchaseState.PENDING,
                        onPurchase = { product ->
                            context.findActivity()?.let { billingManager.purchase(it, product) }
                                ?: billingManager.reportPurchaseUnavailable()
                        },
                    )
                }
            }

            when (purchaseState) {
                DonationPurchaseState.PROCESSING -> Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                ) {
                    CircularProgressIndicator()
                    Text(stringResource(R.string.donation_processing))
                }
                DonationPurchaseState.PENDING -> Text(
                    stringResource(R.string.donation_pending),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                DonationPurchaseState.FAILED -> if (!acknowledgementIssue) Text(
                    stringResource(R.string.donation_payment_failed),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                else -> Unit
            }

            if (acknowledgementIssue) Text(
                stringResource(R.string.donation_acknowledgement_pending),
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )

            when (restoreState) {
                DonationRestoreState.RESTORING -> Text(
                    stringResource(R.string.donation_restoring),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                DonationRestoreState.RESTORED -> Text(
                    stringResource(R.string.donation_restore_success),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                DonationRestoreState.NO_PURCHASES -> Text(
                    stringResource(R.string.donation_restore_empty),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                DonationRestoreState.FAILED -> if (!acknowledgementIssue) Text(
                    stringResource(R.string.donation_restore_failed),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
                DonationRestoreState.IDLE -> Unit
            }

            TextButton(
                onClick = { billingManager.restorePurchases() },
                enabled = restoreState != DonationRestoreState.RESTORING,
            ) {
                Text(stringResource(R.string.donation_restore))
            }
        }
    }
}

@Composable
private fun DonationOptions(
    products: List<ProductDetails>,
    enabled: Boolean,
    onPurchase: (ProductDetails) -> Unit,
) {
    DonationProducts.all.forEachIndexed { index, productId ->
        products.firstOrNull { it.productId == productId }?.let { product ->
            val price = product.oneTimePurchaseOfferDetails?.formattedPrice
            if (price != null) {
                Button(
                    onClick = { onPurchase(product) },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        stringResource(
                            when (index) {
                                0 -> R.string.donation_tier_1
                                1 -> R.string.donation_tier_2
                                else -> R.string.donation_tier_3
                            },
                            price,
                        ),
                    )
                }
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}
