package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen

internal val widgetRefreshFailedKey = booleanPreferencesKey("widget_refresh_failed")

internal val widgetStateDefinition = PreferencesGlanceStateDefinition

internal const val WIDGET_WEATHER_SYNC_WORK = "widget_weather_sync"

internal enum class WidgetDisplayState {
    WEATHER,
    LOADING,
    UNAVAILABLE,
}

internal fun resolveWidgetDisplayState(
    hasWeather: Boolean,
    refreshFailed: Boolean,
    refreshRunning: Boolean?,
): WidgetDisplayState = when {
    hasWeather -> WidgetDisplayState.WEATHER
    refreshFailed -> WidgetDisplayState.UNAVAILABLE
    refreshRunning != false -> WidgetDisplayState.LOADING
    else -> WidgetDisplayState.UNAVAILABLE
}

internal fun widgetWeatherRefreshRunningFlow(context: Context): Flow<Boolean> = flow {
    emitAll(
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(WIDGET_WEATHER_SYNC_WORK)
            .map { workInfos -> workInfos.any { it.state == WorkInfo.State.RUNNING } },
    )
}.retryWhen { failure, attempt ->
    if (failure is CancellationException || attempt >= 3) {
        false
    } else {
        emit(false)
        delay(1_000L shl attempt.toInt())
        true
    }
}.distinctUntilChanged().catch { failure ->
    if (failure is CancellationException) throw failure
    emit(false)
}
