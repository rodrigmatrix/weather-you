package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.datastore.preferences.core.booleanPreferencesKey

internal val widgetRefreshFailedKey = booleanPreferencesKey("widget_refresh_failed")

internal val widgetStateDefinition = PreferencesGlanceStateDefinition

internal enum class WidgetDisplayState {
    WEATHER,
    LOADING,
    UNAVAILABLE,
}

internal fun resolveWidgetDisplayState(
    hasWeather: Boolean,
    refreshFailed: Boolean,
    isConnected: Boolean,
): WidgetDisplayState = when {
    hasWeather -> WidgetDisplayState.WEATHER
    refreshFailed || !isConnected -> WidgetDisplayState.UNAVAILABLE
    else -> WidgetDisplayState.LOADING
}

/** Uses APIs available from Android 6.0 (API 23), the app's supported minimum. */
internal fun Context.hasInternetConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return false
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
