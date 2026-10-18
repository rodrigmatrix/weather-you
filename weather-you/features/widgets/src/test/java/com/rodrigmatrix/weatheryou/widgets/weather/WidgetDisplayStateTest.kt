package com.rodrigmatrix.weatheryou.widgets.weather

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetDisplayStateTest {

    @Test
    fun cachedWeatherWinsEvenWhenOfflineOrRefreshFailed() {
        assertEquals(
            WidgetDisplayState.WEATHER,
            resolveWidgetDisplayState(hasWeather = true, refreshFailed = true, refreshRunning = false),
        )
    }

    @Test
    fun offlineWithoutCachedWeatherIsUnavailable() {
        assertEquals(
            WidgetDisplayState.UNAVAILABLE,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, refreshRunning = false),
        )
    }

    @Test
    fun failedRefreshWithoutCachedWeatherIsUnavailable() {
        assertEquals(
            WidgetDisplayState.UNAVAILABLE,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = true, refreshRunning = true),
        )
    }

    @Test
    fun activeRefreshWithoutCachedWeatherIsLoading() {
        assertEquals(
            WidgetDisplayState.LOADING,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, refreshRunning = true),
        )
    }

    @Test
    fun unknownRefreshStateShowsLoadingUntilWorkManagerEmits() {
        assertEquals(
            WidgetDisplayState.LOADING,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, refreshRunning = null),
        )
    }

    @Test
    fun queuedRefreshWithoutCachedWeatherDoesNotStayLoading() {
        assertEquals(
            WidgetDisplayState.UNAVAILABLE,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, refreshRunning = false),
        )
    }

    @Test
    fun widgetRefreshStopsAfterThreeTotalAttempts() {
        assertTrue(shouldRetryWidgetWeatherSync(runAttemptCount = 0))
        assertTrue(shouldRetryWidgetWeatherSync(runAttemptCount = 1))
        assertFalse(shouldRetryWidgetWeatherSync(runAttemptCount = 2))
        assertFalse(shouldRetryWidgetWeatherSync(runAttemptCount = 3))
    }
}
