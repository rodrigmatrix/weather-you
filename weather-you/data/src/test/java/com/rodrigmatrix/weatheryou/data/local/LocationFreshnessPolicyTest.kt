package com.rodrigmatrix.weatheryou.data.local

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationFreshnessPolicyTest {
    @Test
    fun `accepts a recent cached location including the five minute boundary`() {
        val now = 1_000_000_000_000L

        assertTrue(LocationFreshnessPolicy.isRecent(now, now))
        assertTrue(LocationFreshnessPolicy.isRecent(now - 5 * 60 * 1_000_000_000L, now))
    }

    @Test
    fun `rejects cached locations older than five minutes`() {
        val now = 1_000_000_000_000L

        assertFalse(LocationFreshnessPolicy.isRecent(now - 5 * 60 * 1_000_000_000L - 1_000_000L, now))
    }

    @Test
    fun `rejects an unknown or future monotonic timestamp`() {
        val now = 1_000_000_000_000L

        assertFalse(LocationFreshnessPolicy.isRecent(0L, now))
        assertFalse(LocationFreshnessPolicy.isRecent(now + 1L, now))
    }
}
