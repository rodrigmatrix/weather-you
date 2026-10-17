package com.rodrigmatrix.weatheryou.widgets.weather

import org.junit.Assert.assertEquals
import org.junit.Test

class WidgetDisplayStateTest {

    @Test
    fun cachedWeatherWinsEvenWhenOfflineOrRefreshFailed() {
        assertEquals(
            WidgetDisplayState.WEATHER,
            resolveWidgetDisplayState(hasWeather = true, refreshFailed = true, isConnected = false),
        )
    }

    @Test
    fun offlineWithoutCachedWeatherIsUnavailable() {
        assertEquals(
            WidgetDisplayState.UNAVAILABLE,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, isConnected = false),
        )
    }

    @Test
    fun failedRefreshWithoutCachedWeatherIsUnavailable() {
        assertEquals(
            WidgetDisplayState.UNAVAILABLE,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = true, isConnected = true),
        )
    }

    @Test
    fun connectedPendingRefreshWithoutCachedWeatherIsLoading() {
        assertEquals(
            WidgetDisplayState.LOADING,
            resolveWidgetDisplayState(hasWeather = false, refreshFailed = false, isConnected = true),
        )
    }
}
