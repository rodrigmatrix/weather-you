package com.rodrigmatrix.weatheryou.presentation.donation

import android.app.Activity
import android.content.Context

/** A display-only option. It intentionally has no Billing ProductDetails or price. */
internal data class DonationMockOffer(
    val productId: String,
    val tierIndex: Int,
)

/** Variant implementation keeps the mock catalog unavailable in Google Play release builds. */
internal interface DonationMockCatalogProvider {
    val available: Boolean

    fun isEnabled(context: Context): Boolean

    fun setEnabled(context: Context, enabled: Boolean)

    fun isPromptPreviewEnabled(context: Context): Boolean = false

    fun setPromptPreviewEnabled(context: Context, enabled: Boolean) = Unit

    fun offers(): List<DonationMockOffer>

    fun launchScenario(activity: Activity, productId: String, state: String)
}

/** Intent contract shared by the debug-only catalog launcher and simulator activity. */
internal const val DONATION_MOCK_STATE_EXTRA = "state"
internal const val DONATION_MOCK_PRODUCT_ID_EXTRA = "productId"
