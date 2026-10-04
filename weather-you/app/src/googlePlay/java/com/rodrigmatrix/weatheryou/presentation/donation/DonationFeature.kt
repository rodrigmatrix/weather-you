package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
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
    var mockCatalogEnabled by remember(context) {
        mutableStateOf(DonationMockCatalog.available && DonationMockCatalog.isEnabled(context))
    }
    var selectedMockProductId by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner = context.findActivity() as? LifecycleOwner

    LaunchedEffect(billingManager) { billingManager.connectAndRefresh() }
    DisposableEffect(lifecycleOwner, billingManager) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                mockCatalogEnabled = DonationMockCatalog.available && DonationMockCatalog.isEnabled(context)
                billingManager.connectAndRefresh()
            }
        }
        lifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose { lifecycleOwner?.lifecycle?.removeObserver(observer) }
    }

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Column(
                Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (isDonor) {
                    DonationActiveContent(
                        mockCatalogEnabled = mockCatalogEnabled,
                        onResetMock = {
                            context.findActivity()?.let { activity ->
                                DonationMockCatalog.launchScenario(
                                    activity = activity,
                                    productId = DonationProducts.SUPPORTER,
                                    state = MOCK_RESET,
                                )
                            }
                        },
                    )

                    val remainingMockOffers = DonationMockCatalog.offers()
                        .filterNot { it.productId in ownedProductIds }
                    val remainingPlayProducts = products.filterNot { it.productId in ownedProductIds }
                    val hasAdditionalOptions = if (mockCatalogEnabled) {
                        remainingMockOffers.isNotEmpty()
                    } else {
                        catalogState == DonationCatalogState.AVAILABLE && remainingPlayProducts.isNotEmpty()
                    }

                    if (hasAdditionalOptions && showAdditionalDonations) {
                        Text(stringResource(R.string.donation_additional_note))
                        if (mockCatalogEnabled) {
                            DonationMockOptions(remainingMockOffers, enabled = true) { offer ->
                                selectedMockProductId = offer.productId
                            }
                        } else {
                            DonationOptions(
                                products = remainingPlayProducts,
                                enabled = purchaseState != DonationPurchaseState.PROCESSING &&
                                    purchaseState != DonationPurchaseState.PENDING,
                                onPurchase = { product ->
                                    context.findActivity()?.let { billingManager.purchase(it, product) }
                                        ?: billingManager.reportPurchaseUnavailable()
                                },
                            )
                        }
                    } else if (hasAdditionalOptions) {
                        TextButton(onClick = { showAdditionalDonations = true }) {
                            Text(stringResource(R.string.donation_additional_action))
                        }
                    } else if (!mockCatalogEnabled && catalogState == DonationCatalogState.AVAILABLE) {
                        Text(stringResource(R.string.donation_all_tiers_used))
                    }
                } else {
                    Text(stringResource(R.string.donation_title), style = MaterialTheme.typography.headlineSmall)
                    Text(stringResource(R.string.donation_benefits), style = MaterialTheme.typography.bodyLarge)

                    if (mockCatalogEnabled) {
                        DonationMockBanner(onUseLiveCatalog = {
                            DonationMockCatalog.setEnabled(context, false)
                            mockCatalogEnabled = false
                            billingManager.connectAndRefresh()
                        })
                        DonationMockOptions(DonationMockCatalog.offers(), enabled = true) { offer ->
                            selectedMockProductId = offer.productId
                        }
                    } else {
                        when (catalogState) {
                            DonationCatalogState.LOADING -> DonationLoading()
                            DonationCatalogState.UNAVAILABLE -> {
                                DonationStatusNotice(
                                    message = stringResource(R.string.donation_prices_unavailable),
                                    isError = true,
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = { billingManager.connectAndRefresh() }) {
                                        Text(stringResource(R.string.donation_retry))
                                    }
                                    if (DonationMockCatalog.available) {
                                        TextButton(onClick = {
                                            DonationMockCatalog.setEnabled(context, true)
                                            mockCatalogEnabled = true
                                        }) {
                                            Text(stringResource(R.string.donation_preview_sample_action))
                                        }
                                    }
                                }
                            }
                            DonationCatalogState.AVAILABLE -> DonationOptions(
                                products = products.filterNot { it.productId in ownedProductIds },
                                enabled = purchaseState != DonationPurchaseState.PROCESSING &&
                                    purchaseState != DonationPurchaseState.PENDING,
                                onPurchase = { product ->
                                    context.findActivity()?.let { billingManager.purchase(it, product) }
                                        ?: billingManager.reportPurchaseUnavailable()
                                },
                            )
                        }
                    }
                }

                DonationPurchaseStatus(purchaseState, acknowledgementIssue)
                DonationRestoreStatus(restoreState, acknowledgementIssue)

                TextButton(
                    onClick = { billingManager.restorePurchases() },
                    enabled = restoreState != DonationRestoreState.RESTORING,
                ) {
                    Text(stringResource(R.string.donation_restore))
                }
            }
        }
    }

    selectedMockProductId?.let { productId ->
        val activity = context.findActivity()
        if (activity != null) {
            DonationMockOutcomeDialog(
                onDismiss = { selectedMockProductId = null },
                onChoose = { state ->
                    DonationMockCatalog.launchScenario(activity, productId, state)
                    selectedMockProductId = null
                },
            )
        }
    }
}

@Composable
private fun DonationActiveContent(
    mockCatalogEnabled: Boolean,
    onResetMock: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.large) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.donation_active_title), style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(R.string.donation_thank_you), style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(R.string.donation_active_benefits_title), style = MaterialTheme.typography.titleMedium)
            DonationBenefitList()
            if (mockCatalogEnabled && DonationMockCatalog.available) {
                Text(stringResource(R.string.donation_test_mode_banner), style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = onResetMock) {
                    Text(stringResource(R.string.donation_reset_simulated_action))
                }
            }
        }
    }
}

@Composable
private fun DonationBenefitList() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            R.string.donation_benefit_ads,
            R.string.donation_benefit_locations,
            R.string.donation_benefit_watch,
        ).forEach { benefit ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("✓", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                Text(stringResource(benefit), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun DonationLoading() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        CircularProgressIndicator()
        Text(stringResource(R.string.donation_loading))
    }
}

@Composable
private fun DonationMockBanner(onUseLiveCatalog: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = MaterialTheme.shapes.medium) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.donation_test_mode_banner), style = MaterialTheme.typography.titleSmall)
            Text(stringResource(R.string.donation_mock_catalog_description), style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onUseLiveCatalog) {
                Text(stringResource(R.string.donation_return_to_live_catalog))
            }
        }
    }
}

@Composable
private fun DonationMockOptions(
    offers: List<DonationMockOffer>,
    enabled: Boolean,
    onSelect: (DonationMockOffer) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        offers.forEach { offer ->
            Card {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(tierName(offer.tierIndex), style = MaterialTheme.typography.titleMedium)
                        Text(stringResource(R.string.donation_sample_tier_tag), style = MaterialTheme.typography.labelMedium)
                    }
                    Text(stringResource(R.string.donation_mock_tier_note), style = MaterialTheme.typography.bodyMedium)
                    Button(
                        onClick = { onSelect(offer) },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.donation_preview_tier_action))
                    }
                }
            }
        }
    }
}

@Composable
private fun DonationMockOutcomeDialog(
    onDismiss: () -> Unit,
    onChoose: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.donation_mock_outcome_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.donation_mock_outcome_description))
                TextButton(onClick = { onChoose(MOCK_ACTIVE) }) {
                    Text(stringResource(R.string.donation_mock_success_action))
                }
                TextButton(onClick = { onChoose(MOCK_PENDING) }) {
                    Text(stringResource(R.string.donation_mock_pending_action))
                }
                TextButton(onClick = { onChoose(MOCK_DECLINED) }) {
                    Text(stringResource(R.string.donation_mock_declined_action))
                }
                TextButton(onClick = { onChoose(MOCK_CANCELLED) }) {
                    Text(stringResource(R.string.donation_mock_cancel_action))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.donation_close)) }
        },
    )
}

@Composable
private fun DonationPurchaseStatus(
    state: DonationPurchaseState,
    acknowledgementIssue: Boolean,
) {
    when {
        state == DonationPurchaseState.PROCESSING -> DonationLoading()
        state == DonationPurchaseState.PENDING -> DonationStatusNotice(
            message = stringResource(R.string.donation_pending),
        )
        state == DonationPurchaseState.FAILED && acknowledgementIssue -> DonationStatusNotice(
            message = stringResource(R.string.donation_acknowledgement_pending),
            isError = true,
        )
        state == DonationPurchaseState.FAILED -> DonationStatusNotice(
            message = stringResource(R.string.donation_payment_failed),
            isError = true,
        )
    }
}

@Composable
private fun DonationRestoreStatus(
    state: DonationRestoreState,
    acknowledgementIssue: Boolean,
) {
    val message = when {
        state == DonationRestoreState.RESTORING -> stringResource(R.string.donation_restoring)
        state == DonationRestoreState.RESTORED -> stringResource(R.string.donation_restore_success)
        state == DonationRestoreState.NO_PURCHASES -> stringResource(R.string.donation_restore_empty)
        state == DonationRestoreState.FAILED && !acknowledgementIssue -> stringResource(R.string.donation_restore_failed)
        else -> null
    }
    message?.let { DonationStatusNotice(it, isError = state == DonationRestoreState.FAILED) }
}

@Composable
private fun DonationStatusNotice(message: String, isError: Boolean = false) {
    val background = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
    val foreground = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
    Surface(
        color = background,
        contentColor = foreground,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Text(message, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DonationOptions(
    products: List<ProductDetails>,
    enabled: Boolean,
    onPurchase: (ProductDetails) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        DonationProducts.all.forEachIndexed { index, productId ->
            products.firstOrNull { it.productId == productId }?.let { product ->
                val price = product.oneTimePurchaseOfferDetails?.formattedPrice
                if (price != null) {
                    Card {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(tierName(index), style = MaterialTheme.typography.titleMedium)
                                Text(price, style = MaterialTheme.typography.titleLarge)
                            }
                            Text(tierDescription(index), style = MaterialTheme.typography.bodyMedium)
                            Text(stringResource(R.string.donation_same_benefits_note), style = MaterialTheme.typography.bodySmall)
                            Button(
                                onClick = { onPurchase(product) },
                                enabled = enabled,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.donation_support_action))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun tierName(index: Int): String = stringResource(
    when (index) {
        0 -> R.string.donation_tier_supporter
        1 -> R.string.donation_tier_friend
        else -> R.string.donation_tier_patron
    },
)

@Composable
private fun tierDescription(index: Int): String = stringResource(
    when (index) {
        0 -> R.string.donation_tier_supporter_description
        1 -> R.string.donation_tier_friend_description
        else -> R.string.donation_tier_patron_description
    },
)

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}

private const val MOCK_ACTIVE = "active"
private const val MOCK_PENDING = "pending"
private const val MOCK_DECLINED = "declined"
private const val MOCK_CANCELLED = "cancelled"
private const val MOCK_RESET = "reset"
