package com.rodrigmatrix.weatheryou.widgets.weather

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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
    fun widgetRefreshCompletingWithinDeadlineSucceeds() = runBlocking {
        runWidgetWeatherRefreshWithTimeout(timeoutMillis = 100) {
            delay(1)
        }
    }

    @Test
    fun widgetRefreshTimeoutBecomesAnIOException() = runBlocking {
        val result = runCatching {
            runWidgetWeatherRefreshWithTimeout(timeoutMillis = 1) {
                delay(100)
            }
        }

        assertTrue(result.exceptionOrNull() is IOException)
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
