package com.rodrigmatrix.weatheryou.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rodrigmatrix.weatheryou.core.utils.UnitLocale
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TEMPERATURE_KEY = "temperature_pref"
private const val THEME_KEY = "theme_pref"
private const val COLOR_KEY = "color_pref"

class SettingsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val unitLocale: UnitLocale,
) : SettingsLocalDataSource {

    override fun getTemperaturePreference(): Flow<TemperaturePreference> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(TEMPERATURE_KEY)] ?: when (unitLocale.getDefault()) {
                UnitLocale.Type.Metric -> TemperaturePreference.METRIC.name
                UnitLocale.Type.Imperial -> TemperaturePreference.IMPERIAL.name
            }
        }.map {
                when (it) {
                    TemperaturePreference.METRIC.name -> TemperaturePreference.METRIC
                    else -> TemperaturePreference.IMPERIAL
                }
            }
    }

    override fun setTemperaturePreference(newPreference: TemperaturePreference): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(TEMPERATURE_KEY)] = newPreference.name
            }
            emit(Unit)
        }
    }

    override fun getAppThemePreference(): Flow<AppThemePreference> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(THEME_KEY)] ?: AppThemePreference.SYSTEM_DEFAULT.name
        }.map {
            when (it) {
                AppThemePreference.LIGHT.name -> AppThemePreference.LIGHT
                AppThemePreference.DARK.name -> AppThemePreference.DARK
                else -> AppThemePreference.SYSTEM_DEFAULT
            }
        }
    }

    override fun setAppThemePreference(theme: AppThemePreference): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(THEME_KEY)] = theme.name
            }
            emit(Unit)
        }
    }

    override fun setAppColorPreference(color: AppColorPreference): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(COLOR_KEY)] = color.name
            }
            emit(Unit)
        }
    }

    override fun getAppColorPreference(): Flow<AppColorPreference> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(COLOR_KEY)] ?: AppColorPreference.DYNAMIC.name
        }.map {
            when (it) {
                AppColorPreference.DYNAMIC.name -> AppColorPreference.DYNAMIC
                AppColorPreference.DEFAULT.name -> AppColorPreference.DEFAULT
                AppColorPreference.MOSQUE.name -> AppColorPreference.MOSQUE
                AppColorPreference.DARK_FERN.name -> AppColorPreference.DARK_FERN
                AppColorPreference.FRESH_EGGPLANT.name -> AppColorPreference.FRESH_EGGPLANT
                else -> AppColorPreference.DEFAULT
            }
        }
    }
}