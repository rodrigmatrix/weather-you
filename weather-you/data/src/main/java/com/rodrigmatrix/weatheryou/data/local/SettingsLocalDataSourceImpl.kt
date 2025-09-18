package com.rodrigmatrix.weatheryou.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rodrigmatrix.weatheryou.data.local.model.AppSettingsEntity
import com.rodrigmatrix.weatheryou.data.mapper.mapToDomain
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.Locale

internal const val TEMPERATURE_KEY = "temperature_pref"
internal const val THEME_KEY = "theme_pref"
internal const val COLOR_KEY = "color_pref"
internal const val WEATHER_ANIMATIONS_KEY = "weather_animations_pref"
internal const val THEME_COLOR_WEATHER_ANIMATIONS_KEY = "theme_color_weather_animations_pref"
internal const val APP_SETTINGS_KEY = "app_settings_key"
internal const val IS_PREMIUM_USER_KEY = "is_premium_user_key"
internal const val WIND_UNIT_KEY = "wind_unit_pref"
internal const val PRECIPITATION_UNIT_KEY = "precipitation_unit_pref"
internal const val DISTANCE_UNIT_KEY = "distance_unit_pref"

class SettingsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsLocalDataSource {

    init {
        runBlocking {
            migrateIfNeeded()
        }
    }

    private suspend fun migrateIfNeeded() {
        val currentPrefs = dataStore.data.first()
        val settingsJson = currentPrefs[stringPreferencesKey(APP_SETTINGS_KEY)]

        if (settingsJson != null) {
            val oldSettings = Json.decodeFromString<AppSettingsEntity>(settingsJson).mapToDomain()
            setAppSettings(oldSettings).first()
            dataStore.edit { it.remove(stringPreferencesKey(APP_SETTINGS_KEY)) }
        }
    }

    override fun getAppSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            createSettingsFromIndividualPrefs(preferences)
        }
    }

    override fun setAppSettings(settings: AppSettings): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TEMPERATURE_KEY)] = settings.temperaturePreference.name
            preferences[stringPreferencesKey(WIND_UNIT_KEY)] = settings.windUnitPreference.name
            preferences[stringPreferencesKey(PRECIPITATION_UNIT_KEY)] = settings.precipitationUnitPreference.name
            preferences[stringPreferencesKey(DISTANCE_UNIT_KEY)] = settings.distanceUnitPreference.name
            preferences[stringPreferencesKey(THEME_KEY)] = settings.appThemePreference.name
            preferences[stringPreferencesKey(COLOR_KEY)] = settings.appColorPreference.name
            preferences[booleanPreferencesKey(WEATHER_ANIMATIONS_KEY)] = settings.enableWeatherAnimations
            preferences[booleanPreferencesKey(THEME_COLOR_WEATHER_ANIMATIONS_KEY)] = settings.enableThemeColorWithWeatherAnimations
        }
        emit(Unit)
    }

    private fun createSettingsFromIndividualPrefs(preferences: Preferences): AppSettings {
        val country = Locale.getDefault().country
        val fahrenheitCountries = setOf("US", "BZ", "BS", "KY")
        val usesFahrenheit = fahrenheitCountries.contains(country)

        val imperialCountries = setOf("US", "LR", "MM")
        val usesImperial = imperialCountries.contains(country)

        val temperature = preferences[stringPreferencesKey(TEMPERATURE_KEY)]?.let {
            safeEnumValueOf(it, TemperaturePreference.METRIC)
        } ?: if (usesFahrenheit) {
            TemperaturePreference.IMPERIAL
        } else {
            TemperaturePreference.METRIC
        }

        val wind = preferences[stringPreferencesKey(WIND_UNIT_KEY)]?.let {
            safeEnumValueOf(it, WindUnitPreference.KPH)
        } ?: if (usesImperial) {
            WindUnitPreference.MPH
        } else {
            WindUnitPreference.KPH
        }

        val precipitation = preferences[stringPreferencesKey(PRECIPITATION_UNIT_KEY)]?.let {
            safeEnumValueOf(it, PrecipitationUnitPreference.MM_CM)
        } ?: if (usesImperial) {
            PrecipitationUnitPreference.IN
        } else {
            PrecipitationUnitPreference.MM_CM
        }

        val distance = preferences[stringPreferencesKey(DISTANCE_UNIT_KEY)]?.let {
            safeEnumValueOf(it, DistanceUnitPreference.KM)
        } ?: if (usesImperial) {
            DistanceUnitPreference.MI
        } else {
            DistanceUnitPreference.KM
        }

        return AppSettings(
            temperaturePreference = temperature,
            windUnitPreference = wind,
            precipitationUnitPreference = precipitation,
            distanceUnitPreference = distance,
            appThemePreference = safeEnumValueOf(
                preferences[stringPreferencesKey(THEME_KEY)],
                AppThemePreference.SYSTEM_DEFAULT
            ),
            appColorPreference = safeEnumValueOf(
                preferences[stringPreferencesKey(COLOR_KEY)],
                AppColorPreference.DYNAMIC
            ),
            enableWeatherAnimations = preferences[booleanPreferencesKey(WEATHER_ANIMATIONS_KEY)] ?: true,
            enableThemeColorWithWeatherAnimations = preferences[booleanPreferencesKey(THEME_COLOR_WEATHER_ANIMATIONS_KEY)] ?: false
        )
    }


    override fun getIsPremiumUser(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(IS_PREMIUM_USER_KEY)] ?: false
        }
    }

    override fun setIsPremiumUser(premium: Boolean): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(IS_PREMIUM_USER_KEY)] = premium
        }
        emit(Unit)
    }

    private inline fun <reified T : Enum<T>> safeEnumValueOf(name: String?, default: T): T {
        return try {
            if (name != null) enumValueOf<T>(name) else default
        } catch (_: IllegalArgumentException) {
            default
        }
    }
}