package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context

/** Release builds have no sample offers or simulator entry point. */
internal object DonationMockCatalog : DonationMockCatalogProvider {
    override val available: Boolean = false

    override fun isEnabled(context: Context): Boolean = false

    override fun setEnabled(context: Context, enabled: Boolean) = Unit

    override fun offers(): List<DonationMockOffer> = emptyList()

    override fun launchScenario(activity: Activity, productId: String, state: String) = Unit
}
