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

    @Test
    fun `current location GPS drift reuses unexpired weather cache`() {
        assertFalse(
            WeatherRefreshPolicy.shouldRefreshCurrentLocationWeather(
                forceUpdate = false,
                weatherExpired = false,
                cachedLatitude = 40.7128,
                cachedLongitude = -74.0060,
                currentLatitude = 40.72,
                currentLongitude = -74.0060,
            )
        )
    }

    @Test
    fun `current location move beyond five kilometers refreshes weather`() {
        assertTrue(
            WeatherRefreshPolicy.shouldRefreshCurrentLocationWeather(
                forceUpdate = false,
                weatherExpired = false,
                cachedLatitude = 0.0,
                cachedLongitude = 0.0,
                currentLatitude = 0.05,
                currentLongitude = 0.0,
            )
        )
    }

    @Test
    fun `missing expired or forced current weather always refreshes`() {
        assertTrue(WeatherRefreshPolicy.shouldRefreshCurrentLocationWeather(false, false, null, null, 0.0, 0.0))
        assertTrue(WeatherRefreshPolicy.shouldRefreshCurrentLocationWeather(false, true, 0.0, 0.0, 0.001, 0.0))
        assertTrue(WeatherRefreshPolicy.shouldRefreshCurrentLocationWeather(true, false, 0.0, 0.0, 0.001, 0.0))
    }
}
