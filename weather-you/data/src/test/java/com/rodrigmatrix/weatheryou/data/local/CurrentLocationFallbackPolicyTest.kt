package com.rodrigmatrix.weatheryou.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrentLocationFallbackPolicyTest {
    @Test
    fun `uses a localized fallback when reverse geocoding has no name`() {
        assertEquals("Current location", CurrentLocationFallbackPolicy.resolveName(null, "Current location"))
        assertEquals("Current location", CurrentLocationFallbackPolicy.resolveName("  ", "Current location"))
    }

    @Test
    fun `preserves the reverse geocoded name when available`() {
        assertEquals("San Jose,California,United States", CurrentLocationFallbackPolicy.resolveName("San Jose,California,United States", "Current location"))
    }
}
