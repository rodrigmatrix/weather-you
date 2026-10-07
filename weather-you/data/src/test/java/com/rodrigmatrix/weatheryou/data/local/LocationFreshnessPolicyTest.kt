package com.rodrigmatrix.weatheryou.data.local

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
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

    @Test
    fun `newest recent location ignores stale provider fixes`() {
        val now = 1_000_000_000_000L
        val stale = LocationFix(now - 10 * 60 * 1_000_000_000L)
        val recent = LocationFix(now - 30 * 1_000_000_000L)
        val newer = LocationFix(now - 10 * 1_000_000_000L)

        assertSame(
            newer,
            LocationFreshnessPolicy.newestRecent(listOf(stale, recent, newer), now, LocationFix::elapsedRealtimeNanos),
        )
    }

    @Test
    fun `newest recent location returns null when every provider fix is stale`() {
        val now = 1_000_000_000_000L
        val stale = LocationFix(now - 10 * 60 * 1_000_000_000L)

        assertNull(
            LocationFreshnessPolicy.newestRecent(listOf(stale), now, LocationFix::elapsedRealtimeNanos),
        )
    }

    private data class LocationFix(val elapsedRealtimeNanos: Long)
}
