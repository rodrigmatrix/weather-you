package com.rodrigmatrix.weatheryou.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rodrigmatrix.weatheryou.core.utils.UnitLocale
import com.rodrigmatrix.weatheryou.data.local.model.AppSettingsEntity
import com.rodrigmatrix.weatheryou.data.mapper.mapToDomain
import com.rodrigmatrix.weatheryou.data.mapper.mapToEntity
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal const val TEMPERATURE_KEY = "temperature_pref"
internal const val THEME_KEY = "theme_pref"
internal const val COLOR_KEY = "color_pref"
internal const val WEATHER_ANIMATIONS_KEY = "weather_animations_pref"
internal const val THEME_COLOR_WEATHER_ANIMATIONS_KEY = "theme_color_weather_animations_pref"
internal const val APP_SETTINGS_KEY = "app_settings_key"

class SettingsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val unitLocale: UnitLocale,
) : SettingsLocalDataSource {

    override fun getAppSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            val preferenceString = preferences[stringPreferencesKey(APP_SETTINGS_KEY)]
            if (preferenceString != null) {
                Json.decodeFromString<AppSettingsEntity>(preferenceString).mapToDomain()
            } else {
                AppSettings(
                    temperaturePreference = getTemperaturePreference(preferences),
                    appThemePreference = getAppThemePreference(preferences),
                    appColorPreference = getAppColorPreference(preferences),
                    enableWeatherAnimations = getIsWeatherAnimationsEnabled(preferences),
                    enableThemeColorWithWeatherAnimations = getIsThemeColorWithWeatherAnimationsEnabled(preferences),
                )
            }
        }
    }

    override fun setAppSettings(settings: AppSettings): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(APP_SETTINGS_KEY)] = Json.encodeToString(
                    settings.mapToEntity()
                )
            }
            emit(Unit)
        }
    }

    private fun getTemperaturePreference(preferences: Preferences): TemperaturePreference {
        val temp = preferences[stringPreferencesKey(TEMPERATURE_KEY)] ?: when (unitLocale.getDefault()) {
            UnitLocale.Type.Metric -> TemperaturePreference.METRIC.name
            UnitLocale.Type.Imperial -> TemperaturePreference.IMPERIAL.name
        }
        return when (temp) {
            TemperaturePreference.METRIC.name -> TemperaturePreference.METRIC
            else -> TemperaturePreference.IMPERIAL
        }
    }

    private fun getAppThemePreference(preferences: Preferences): AppThemePreference {
        val pref = preferences[stringPreferencesKey(THEME_KEY)] ?: AppThemePreference.SYSTEM_DEFAULT.name
        return when (pref) {
            AppThemePreference.LIGHT.name -> AppThemePreference.LIGHT
            AppThemePreference.DARK.name -> AppThemePreference.DARK
            else -> AppThemePreference.SYSTEM_DEFAULT
        }
    }

    private fun getAppColorPreference(preferences: Preferences): AppColorPreference {
        val pref = preferences[stringPreferencesKey(COLOR_KEY)] ?: AppColorPreference.DYNAMIC.name
        return  when (pref) {
            AppColorPreference.DYNAMIC.name -> AppColorPreference.DYNAMIC
            AppColorPreference.DEFAULT.name -> AppColorPreference.DEFAULT
            AppColorPreference.MOSQUE.name -> AppColorPreference.MOSQUE
            AppColorPreference.DARK_FERN.name -> AppColorPreference.DARK_FERN
            AppColorPreference.FRESH_EGGPLANT.name -> AppColorPreference.FRESH_EGGPLANT
            AppColorPreference.CARMINE.name -> AppColorPreference.CARMINE
            AppColorPreference.CINNAMON.name -> AppColorPreference.CINNAMON
            AppColorPreference.PERU_TAN.name -> AppColorPreference.PERU_TAN
            AppColorPreference.GIGAS.name -> AppColorPreference.GIGAS
            else -> AppColorPreference.DEFAULT
        }
    }

    private fun getIsWeatherAnimationsEnabled(preferences: Preferences): Boolean {
        return preferences[booleanPreferencesKey(WEATHER_ANIMATIONS_KEY)] ?: true
    }

    private fun getIsThemeColorWithWeatherAnimationsEnabled(preferences: Preferences): Boolean {
        return preferences[booleanPreferencesKey(THEME_COLOR_WEATHER_ANIMATIONS_KEY)] ?: false
    }
}