package com.rodrigmatrix.weatheryou.widgets.weather

import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.datastore.preferences.core.booleanPreferencesKey

internal val widgetRefreshFailedKey = booleanPreferencesKey("widget_refresh_failed")

internal val widgetStateDefinition = PreferencesGlanceStateDefinition
