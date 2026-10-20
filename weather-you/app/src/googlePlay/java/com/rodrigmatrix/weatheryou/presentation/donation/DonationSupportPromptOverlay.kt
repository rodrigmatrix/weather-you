package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selectableGroup
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
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.home.presentation.home.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import org.koin.compose.koinInject

private const val SUPPORT_PROMPT_COOLDOWN_MILLIS = 30L * 24L * 60L * 60L * 1000L

@Composable
fun DonationSupportPromptOverlay(
    homeUiState: HomeUiState,
    isLocationsScreen: Boolean,
    modifier: Modifier = Modifier,
    billingManager: DonationBillingManager = koinInject(),
    settingsRepository: SettingsRepository = koinInject(),
) {
    val context = LocalContext.current
    val isDonor by settingsRepository.getIsPremiumUser().collectAsState(initial = false)
    val products by billingManager.products.collectAsState()
    val catalogState by billingManager.catalogState.collectAsState()
    val purchaseState by billingManager.purchaseState.collectAsState()
    val ownedProductIds by billingManager.ownedProductIds.collectAsState()
    val purchasesReconciled by billingManager.purchasesReconciled.collectAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = context.findActivity() as? LifecycleOwner
    var mockCatalogEnabled by remember(context) {
        mutableStateOf(DonationMockCatalog.available && DonationMockCatalog.isEnabled(context))
    }
    var promptPreviewEnabled by remember(context) {
        mutableStateOf(DonationMockCatalog.available && DonationMockCatalog.isPromptPreviewEnabled(context))
    }
    var dismissedAtMillis by remember { mutableStateOf<Long?>(null) }
    var dismissalPreferenceLoaded by remember { mutableStateOf(false) }
    var forecastSeenBeforeSession by remember { mutableStateOf(false) }
    var forecastPreferenceLoaded by remember { mutableStateOf(false) }
    var isVisible by rememberSaveable { mutableStateOf(false) }
    var shownThisSession by rememberSaveable { mutableStateOf(false) }
    var selectedProductId by rememberSaveable { mutableStateOf<String?>(null) }
    var showMockOutcome by remember { mutableStateOf(false) }

    LaunchedEffect(settingsRepository) {
        dismissedAtMillis = settingsRepository.getSupportPromptDismissedAtMillis().first()
        forecastSeenBeforeSession = settingsRepository.getHasSeenUsableForecast().first()
        dismissalPreferenceLoaded = true
        forecastPreferenceLoaded = true
    }
    LaunchedEffect(billingManager) { billingManager.connectAndRefresh() }
    DisposableEffect(lifecycleOwner, billingManager) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                mockCatalogEnabled = DonationMockCatalog.available && DonationMockCatalog.isEnabled(context)
                promptPreviewEnabled = DonationMockCatalog.available &&
                    DonationMockCatalog.isPromptPreviewEnabled(context)
                billingManager.connectAndRefresh()
            }
        }
        lifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose { lifecycleOwner?.lifecycle?.removeObserver(observer) }
    }

    val billingOwnershipKnown = purchasesReconciled || mockCatalogEnabled
    val eligible = promptPreviewEnabled || (isLocationsScreen &&
        homeUiState.isLocationsLoaded &&
        !homeUiState.isLoading &&
        homeUiState.locationsList.isNotEmpty() &&
        forecastSeenBeforeSession &&
        homeUiState.dialogState == com.rodrigmatrix.weatheryou.home.presentation.home.HomeDialogState.Hidden)
    val notCoolingDown = isSupportPromptCooldownComplete(
        lastDismissedAtMillis = dismissedAtMillis,
        nowMillis = System.currentTimeMillis(),
    )

    val shouldAutoShow = shouldAutoShowSupportPrompt(
        isEligible = eligible,
        billingOwnershipKnown = billingOwnershipKnown,
        hasDonorEntitlement = isDonor,
        hasOwnedProducts = ownedProductIds.isNotEmpty(),
        purchaseInProgress = purchaseState == DonationPurchaseState.PROCESSING ||
            purchaseState == DonationPurchaseState.PENDING,
        dismissalPreferenceLoaded = dismissalPreferenceLoaded,
        forecastPreferenceLoaded = forecastPreferenceLoaded,
        cooldownComplete = notCoolingDown || promptPreviewEnabled,
        shownThisSession = shownThisSession,
    )
    LaunchedEffect(shouldAutoShow) {
        if (shouldAutoShow) {
            isVisible = true
            shownThisSession = true
        }
    }
    LaunchedEffect(isDonor, purchaseState) {
        if (isDonor && purchaseState != DonationPurchaseState.ACTIVE &&
            purchaseState != DonationPurchaseState.PROCESSING
        ) {
            isVisible = false
        }
    }

    fun dismissPrompt() {
        val shouldPersistDismissal = !isDonor && purchaseState != DonationPurchaseState.ACTIVE
        isVisible = false
        if (shouldPersistDismissal) {
            val now = System.currentTimeMillis()
            dismissedAtMillis = now
            scope.launch {
                settingsRepository.setSupportPromptDismissedAtMillis(now)
                    .flowOn(Dispatchers.IO)
                    .first()
            }
        }
    }

    BackHandler(enabled = isVisible) { dismissPrompt() }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        val selectedOffer = buildList {
            if (mockCatalogEnabled) {
                DonationMockCatalog.offers().forEach { mock ->
                    add(DonationPromptOffer(mock.productId, mock.tierIndex, null, null))
                }
            } else {
                DonationProducts.all.forEachIndexed { index, productId ->
                    val product = products.firstOrNull { it.productId == productId }
                    val price = product?.oneTimePurchaseOfferDetails?.formattedPrice
                    if (product != null && price != null && productId !in ownedProductIds) {
                        add(DonationPromptOffer(productId, index, price, product))
                    }
                }
            }
        }
        LaunchedEffect(selectedOffer) {
            if (selectedProductId !in selectedOffer.map { it.productId }) {
                selectedProductId = selectedOffer.firstOrNull()?.productId
            }
        }
        val activePurchaseJustMade = purchaseState == DonationPurchaseState.ACTIVE && isDonor
        DonationConversionScreen(
            offers = selectedOffer,
            selectedProductId = selectedProductId,
            catalogState = catalogState,
            purchaseState = purchaseState,
            isMockCatalog = mockCatalogEnabled,
            showSuccess = activePurchaseJustMade,
            onDismiss = ::dismissPrompt,
            onSelectOffer = { selectedProductId = it },
            onRetryCatalog = billingManager::connectAndRefresh,
            onPurchase = { offer ->
                if (offer.productDetails != null) {
                    context.findActivity()?.let { billingManager.purchase(it, offer.productDetails) }
                        ?: billingManager.reportPurchaseUnavailable()
                } else if (mockCatalogEnabled) {
                    showMockOutcome = true
                }
            },
        )
        if (showMockOutcome) {
            val activity = context.findActivity()
            if (activity != null && selectedProductId != null) {
                DonationMockOutcomeDialogForPrompt(
                    onDismiss = { showMockOutcome = false },
                    onChoose = { state ->
                        DonationMockCatalog.launchScenario(activity, selectedProductId!!, state)
                        showMockOutcome = false
                    },
                )
            }
        }
    }
}

private data class DonationPromptOffer(
    val productId: String,
    val tierIndex: Int,
    val formattedPrice: String?,
    val productDetails: ProductDetails?,
)

internal fun isSupportPromptCooldownComplete(lastDismissedAtMillis: Long?, nowMillis: Long): Boolean {
    if (lastDismissedAtMillis == null) return true
    if (nowMillis < lastDismissedAtMillis) return false
    return nowMillis - lastDismissedAtMillis >= SUPPORT_PROMPT_COOLDOWN_MILLIS
}

internal fun shouldAutoShowSupportPrompt(
    isEligible: Boolean,
    billingOwnershipKnown: Boolean,
    hasDonorEntitlement: Boolean,
    hasOwnedProducts: Boolean,
    purchaseInProgress: Boolean,
    dismissalPreferenceLoaded: Boolean,
    forecastPreferenceLoaded: Boolean,
    cooldownComplete: Boolean,
    shownThisSession: Boolean,
): Boolean = isEligible && billingOwnershipKnown && !hasDonorEntitlement && !hasOwnedProducts &&
    !purchaseInProgress && dismissalPreferenceLoaded && forecastPreferenceLoaded && cooldownComplete &&
    !shownThisSession

@Composable
private fun DonationConversionScreen(
    offers: List<DonationPromptOffer>,
    selectedProductId: String?,
    catalogState: DonationCatalogState,
    purchaseState: DonationPurchaseState,
    isMockCatalog: Boolean,
    showSuccess: Boolean,
    onDismiss: () -> Unit,
    onSelectOffer: (String) -> Unit,
    onRetryCatalog: () -> Unit,
    onPurchase: (DonationPromptOffer) -> Unit,
) {
    val selectedOffer = offers.firstOrNull { it.productId == selectedProductId }
    val screenTitle = stringResource(R.string.donation_conversion_headline)
    val purchaseInProgress = purchaseState == DonationPurchaseState.PROCESSING ||
        purchaseState == DonationPurchaseState.PENDING
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .semantics { paneTitle = screenTitle },
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            contentAlignment = Alignment.TopCenter,
        ) {
            val isWide = maxWidth >= 720.dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 960.dp)
                    .padding(horizontal = if (isWide) 32.dp else 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.donation_not_now)) }
                }

                if (showSuccess) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        DonationPromptSuccess(onDismiss = onDismiss)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(76.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(34.dp),
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.donation_conversion_headline),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = stringResource(R.string.donation_conversion_intro),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (isWide) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                DonationPromptBenefits(Modifier.weight(1f))
                                DonationPromptChoices(
                                    offers = offers,
                                    selectedProductId = selectedProductId,
                                    isMockCatalog = isMockCatalog,
                                    modifier = Modifier.weight(1f),
                                    onSelect = onSelectOffer,
                                )
                            }
                        } else {
                            DonationPromptBenefits(Modifier.fillMaxWidth())
                            DonationPromptChoices(
                                offers = offers,
                                selectedProductId = selectedProductId,
                                isMockCatalog = isMockCatalog,
                                modifier = Modifier.fillMaxWidth(),
                                onSelect = onSelectOffer,
                            )
                        }

                        when {
                            isMockCatalog -> Text(
                                stringResource(R.string.donation_test_mode_banner),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                            catalogState == DonationCatalogState.LOADING -> DonationPromptLoading()
                            catalogState == DonationCatalogState.UNAVAILABLE || offers.isEmpty() -> {
                                DonationPromptNotice(
                                    stringResource(R.string.donation_prices_unavailable),
                                    isError = true,
                                )
                                TextButton(onClick = onRetryCatalog) {
                                    Text(stringResource(R.string.donation_retry))
                                }
                            }
                        }

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalArrangement = if (isWide) Arrangement.End else Arrangement.Center,
                    ) {
                        Column(
                            modifier = if (isWide) Modifier.fillMaxWidth(0.5f) else Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                        when (purchaseState) {
                            DonationPurchaseState.PROCESSING -> DonationPromptLoading(
                                message = stringResource(R.string.donation_processing),
                            )
                            DonationPurchaseState.PENDING -> DonationPromptNotice(
                                stringResource(R.string.donation_pending),
                            )
                            DonationPurchaseState.FAILED -> DonationPromptNotice(
                                stringResource(R.string.donation_payment_failed),
                                isError = true,
                            )
                            else -> Unit
                        }
                        Button(
                            onClick = { selectedOffer?.let(onPurchase) },
                            enabled = selectedOffer != null && !purchaseInProgress &&
                                (isMockCatalog || catalogState == DonationCatalogState.AVAILABLE),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = when {
                                    isMockCatalog -> stringResource(
                                        R.string.donation_conversion_preview_tier_action,
                                        selectedOffer?.let { promptTierName(it.tierIndex) }.orEmpty(),
                                    )
                                    selectedOffer?.formattedPrice != null -> stringResource(
                                        R.string.donation_conversion_continue,
                                        selectedOffer.let { promptTierName(it.tierIndex) },
                                        selectedOffer.formattedPrice,
                                    )
                                    else -> stringResource(R.string.donation_support_action)
                                },
                            )
                        }
                        Text(
                            text = stringResource(R.string.donation_conversion_once_note),
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DonationPromptBenefits(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                stringResource(R.string.donation_conversion_benefits_title),
                style = MaterialTheme.typography.titleLarge,
            )
            listOf(
                R.string.donation_benefit_ads,
                R.string.donation_benefit_locations,
                R.string.donation_benefit_watch,
            ).forEach { benefit ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(stringResource(benefit), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun DonationPromptChoices(
    offers: List<DonationPromptOffer>,
    selectedProductId: String?,
    isMockCatalog: Boolean,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier = modifier.semantics { selectableGroup() },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        offers.forEach { offer ->
            Surface(
                shape = MaterialTheme.shapes.large,
                color = if (offer.productId == selectedProductId) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(
                    width = if (offer.productId == selectedProductId) 2.dp else 1.dp,
                    color = if (offer.productId == selectedProductId) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.outlineVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = offer.productId == selectedProductId,
                        role = Role.RadioButton,
                        onClick = { onSelect(offer.productId) },
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(promptTierName(offer.tierIndex), style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (isMockCatalog) stringResource(R.string.donation_sample_tier_tag)
                            else stringResource(R.string.donation_tier_one_time),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    offer.formattedPrice?.let {
                        Text(it, style = MaterialTheme.typography.titleMedium)
                    }
                    RadioButton(
                        selected = offer.productId == selectedProductId,
                        onClick = null,
                    )
                }
            }
        }
        Text(
            stringResource(R.string.donation_same_benefits_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DonationPromptSuccess(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Spacer(Modifier.height(12.dp))
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp),
        )
        Text(stringResource(R.string.donation_active_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            stringResource(R.string.donation_thank_you),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        DonationPromptBenefits(Modifier.fillMaxWidth())
        Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.donation_conversion_back_to_weather))
        }
    }
}

@Composable
private fun DonationPromptLoading(message: String = stringResource(R.string.donation_loading)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        Text(message, modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DonationPromptNotice(message: String, isError: Boolean = false) {
    val background = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
    val foreground = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
    Surface(
        color = background,
        contentColor = foreground,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Text(message, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DonationMockOutcomeDialogForPrompt(
    onDismiss: () -> Unit,
    onChoose: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.donation_mock_outcome_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.donation_mock_outcome_description))
                TextButton(onClick = { onChoose(MOCK_ACTIVE) }) { Text(stringResource(R.string.donation_mock_success_action)) }
                TextButton(onClick = { onChoose(MOCK_PENDING) }) { Text(stringResource(R.string.donation_mock_pending_action)) }
                TextButton(onClick = { onChoose(MOCK_DECLINED) }) { Text(stringResource(R.string.donation_mock_declined_action)) }
                TextButton(onClick = { onChoose(MOCK_CANCELLED) }) { Text(stringResource(R.string.donation_mock_cancel_action)) }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.donation_close)) }
        },
    )
}

@Composable
private fun promptTierName(index: Int): String = stringResource(
    when (index) {
        0 -> R.string.donation_tier_supporter
        1 -> R.string.donation_tier_friend
        else -> R.string.donation_tier_patron
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
