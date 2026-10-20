package com.rodrigmatrix.weatheryou.presentation.donation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DonationSupportPromptCooldownTest {
    @Test
    fun promptIsEligibleWithoutPriorDismissal() {
        assertTrue(isSupportPromptCooldownComplete(lastDismissedAtMillis = null, nowMillis = 0L))
    }

    @Test
    fun promptWaitsUntilThirtyDaysAfterDismissal() {
        val dismissedAt = 1_000L
        val twentyNineDays = dismissedAt + 29L * 24L * 60L * 60L * 1_000L
        val thirtyDays = dismissedAt + 30L * 24L * 60L * 60L * 1_000L

        assertFalse(isSupportPromptCooldownComplete(dismissedAt, twentyNineDays))
        assertTrue(isSupportPromptCooldownComplete(dismissedAt, thirtyDays))
    }

    @Test
    fun clockRollbackDoesNotPrematurelyShowPrompt() {
        assertFalse(isSupportPromptCooldownComplete(lastDismissedAtMillis = 2_000L, nowMillis = 1_000L))
    }

    @Test
    fun autoPromptWaitsForLocationsBillingAndDismissalState() {
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = false,
                billingOwnershipKnown = true,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = false,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = true,
                hasDonorEntitlement = true,
                hasOwnedProducts = true,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = true,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = false,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
        assertTrue(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = true,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
    }

    @Test
    fun autoPromptWaitsUntilForecastHistoryIsLoaded() {
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = true,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = false,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = false,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
    }

    @Test
    fun autoPromptWaitsForPendingOrProcessingPurchase() {
        assertFalse(
            shouldAutoShowSupportPrompt(
                isEligible = true,
                billingOwnershipKnown = true,
                hasDonorEntitlement = false,
                hasOwnedProducts = false,
                purchaseInProgress = true,
                dismissalPreferenceLoaded = true,
                forecastPreferenceLoaded = true,
                cooldownComplete = true,
                shownThisSession = false,
            ),
        )
    }
}
