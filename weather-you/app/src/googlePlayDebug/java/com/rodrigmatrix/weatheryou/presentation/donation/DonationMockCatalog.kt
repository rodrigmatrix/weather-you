package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.rodrigmatrix.weatheryou.billing.DonationProducts

internal object DonationMockCatalog : DonationMockCatalogProvider {
    override val available: Boolean = true

    override fun isEnabled(context: Context): Boolean = context
        .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        .getBoolean(KEY_ENABLED, false)

    override fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }

    override fun offers(): List<DonationMockOffer> = DonationProducts.all.mapIndexed { index, productId ->
        DonationMockOffer(productId = productId, tierIndex = index)
    }

    override fun launchScenario(activity: Activity, productId: String, state: String) {
        if (!isEnabled(activity) || productId !in DonationProducts.all) return
        activity.startActivity(
            Intent(activity, DonationStateSimulatorActivity::class.java)
                .putExtra(DONATION_MOCK_STATE_EXTRA, state)
                .putExtra(DONATION_MOCK_PRODUCT_ID_EXTRA, productId),
        )
    }

    private const val PREFERENCES = "donation_debug_preferences"
    private const val KEY_ENABLED = "mock_catalog_enabled"
}
