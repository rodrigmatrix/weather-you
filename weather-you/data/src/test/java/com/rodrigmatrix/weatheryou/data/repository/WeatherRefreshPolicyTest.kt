package com.rodrigmatrix.weatheryou.data.repository

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRefreshPolicyTest {
    @Test
    fun `non-forced polling never forces weather requests`() {
        assertFalse(WeatherRefreshPolicy.shouldForceCurrentLocation(false, null))
        assertFalse(WeatherRefreshPolicy.shouldForceSavedLocation(false, null, null, null, 1.0, 2.0))
    }

    @Test
    fun `legacy forced refresh without target forces all locations`() {
        assertTrue(WeatherRefreshPolicy.shouldForceCurrentLocation(true, null))
        assertTrue(WeatherRefreshPolicy.shouldForceSavedLocation(true, null, null, null, 1.0, 2.0))
    }

    @Test
    fun `current location target forces only current location`() {
        assertTrue(WeatherRefreshPolicy.shouldForceCurrentLocation(true, true))
        assertFalse(WeatherRefreshPolicy.shouldForceSavedLocation(true, true, 1.0, 2.0, 1.0, 2.0))
    }

    @Test
    fun `saved location target forces only matching coordinates`() {
        assertFalse(WeatherRefreshPolicy.shouldForceCurrentLocation(true, false))
        assertTrue(WeatherRefreshPolicy.shouldForceSavedLocation(true, false, 1.0, 2.0, 1.0, 2.0))
        assertFalse(WeatherRefreshPolicy.shouldForceSavedLocation(true, false, 1.0, 2.0, 3.0, 4.0))
    }
}
