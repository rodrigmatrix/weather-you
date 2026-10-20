package com.rodrigmatrix.weatheryou.presentation.donation

import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.rodrigmatrix.weatheryou.billing.DonationBillingManager
import com.rodrigmatrix.weatheryou.billing.DonationProducts
import com.rodrigmatrix.weatheryou.presentation.navigation.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.getKoin
import kotlinx.coroutines.launch

/** Debug-only entry point for exercising purchase callback UI without Play Store billing. */
class DonationStateSimulatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state = intent.getStringExtra(DONATION_MOCK_STATE_EXTRA)
        val message = TextView(this).apply {
            text = "Preparing local donation test: ${state ?: "missing"}"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        setContentView(message)

        when (state) {
            "prompt" -> {
                DonationMockCatalog.setEnabled(this, true)
                DonationMockCatalog.setPromptPreviewEnabled(this, true)
                returnToMain()
                return
            }
            "offers" -> {
                DonationMockCatalog.setEnabled(this, true)
                DonationMockCatalog.setPromptPreviewEnabled(this, false)
                returnToMain()
                return
            }
            "live" -> {
                DonationMockCatalog.setEnabled(this, false)
                DonationMockCatalog.setPromptPreviewEnabled(this, false)
                returnToMain()
                return
            }
            "reset" -> {
                lifecycleScope.launch {
                    getKoin().get<DonationBillingManager>().resetSimulatedDonationForDebug()
                    DonationMockCatalog.setEnabled(this@DonationStateSimulatorActivity, false)
                    DonationMockCatalog.setPromptPreviewEnabled(this@DonationStateSimulatorActivity, false)
                    returnToMain()
                }
                return
            }
        }

        runCatching { applyState(state) }
            .onFailure { message.text = "Could not simulate state: ${it.javaClass.simpleName}" }
        Handler(Looper.getMainLooper()).postDelayed({
            val manager = getKoin().get<DonationBillingManager>()
            Log.i(
                LOG_TAG,
                "scenario=$state purchase=${manager.purchaseState.value} restore=${manager.restoreState.value} " +
                    "acknowledgementIssue=${manager.acknowledgementIssue.value}",
            )
        }, 750)

        Handler(Looper.getMainLooper()).postDelayed({
            returnToMain()
        }, 1_500)
    }

    private fun returnToMain() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
        )
        finish()
    }

    private fun applyState(state: String?) {
        val billingManager = getKoin().get<DonationBillingManager>()
        when (state) {
            "declined" -> billingManager.onPurchasesUpdated(billingResult(BillingClient.BillingResponseCode.ERROR), null)
            "cancelled" -> billingManager.onPurchasesUpdated(
                billingResult(BillingClient.BillingResponseCode.USER_CANCELED),
                null,
            )
            "pending" -> simulatePurchase(billingManager, Purchase.PurchaseState.PENDING, acknowledged = false)
            "active" -> simulatePurchase(billingManager, Purchase.PurchaseState.PURCHASED, acknowledged = true)
            else -> error("Use one of: active, cancelled, declined, pending")
        }
    }

    private fun billingResult(responseCode: Int) = BillingResult.newBuilder()
        .setResponseCode(responseCode)
        .setDebugMessage("Debug-only simulated billing callback")
        .build()

    private fun simulatePurchase(manager: DonationBillingManager, state: Int, acknowledged: Boolean) {
        val purchase = testPurchase(state, acknowledged)
        Log.i(LOG_TAG, "simulated product=${purchase.products.firstOrNull()} state=${purchase.purchaseState} acknowledged=${purchase.isAcknowledged}")
        manager.onPurchasesUpdated(
            billingResult(BillingClient.BillingResponseCode.OK),
            mutableListOf(purchase),
        )
    }

    private fun testPurchase(state: Int, acknowledged: Boolean): Purchase {
        val productId = intent.getStringExtra(DONATION_MOCK_PRODUCT_ID_EXTRA)
            ?.takeIf { it in DonationProducts.all }
            ?: DonationProducts.all.first()
        // Billing Library 8 maps the raw Play JSON value 4 to PurchaseState.PENDING (2).
        val serializedState = if (state == Purchase.PurchaseState.PENDING) 4 else state
        val json = JSONObject()
            .put("orderId", "debug-simulated-order")
            .put("packageName", packageName)
            .put("productIds", JSONArray().put(productId))
            .put("purchaseTime", System.currentTimeMillis())
            .put("purchaseState", serializedState)
            .put("purchaseToken", "debug-simulated-token")
            .put("acknowledged", acknowledged)
            .put("autoRenewing", false)
        return Purchase(json.toString(), "debug-simulated-signature")
    }

    private companion object {
        const val LOG_TAG = "DonationStateSimulator"
    }
}
