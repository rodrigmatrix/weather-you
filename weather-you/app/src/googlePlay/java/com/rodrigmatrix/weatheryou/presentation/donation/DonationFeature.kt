package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.billing.DonationBillingManager
import com.rodrigmatrix.weatheryou.billing.DonationProducts
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
    val ready by billingManager.ready.collectAsState()
    val acknowledgementIssue by billingManager.acknowledgementIssue.collectAsState()

    LaunchedEffect(billingManager) { billingManager.connectAndRefresh() }

    Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(stringResource(R.string.donation_title))
            Text(stringResource(R.string.donation_benefits))
            if (isDonor) Text(stringResource(R.string.donation_thank_you))
            if (acknowledgementIssue) Text(stringResource(R.string.donation_acknowledgement_pending))
            DonationProducts.all.forEachIndexed { index, productId ->
                products.firstOrNull { it.productId == productId }?.let { product ->
                    val price = product.oneTimePurchaseOfferDetails?.formattedPrice
                    if (price != null) {
                        Button(
                            onClick = { context.findActivity()?.let { billingManager.purchase(it, product) } },
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
            if (!ready || products.isEmpty()) Text(stringResource(R.string.donation_prices_unavailable))
            TextButton(onClick = { billingManager.connectAndRefresh() }) {
                Text(stringResource(R.string.donation_restore))
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}
