package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TEMPERATURE_KEY = "temperature_pref"
private const val THEME_KEY = "theme_pref"

class SettingsLocalDataSourceImpl(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : SettingsLocalDataSource {

    override fun getTemperaturePreference(): Flow<TemperaturePreference> {
        return sharedPreferencesDataSource.getString(
            key = TEMPERATURE_KEY,
            defaultValue = TemperaturePreference.METRIC.name
        ).map {
                when (it) {
                    TemperaturePreference.METRIC.name -> TemperaturePreference.METRIC
                    else -> TemperaturePreference.IMPERIAL
                }
            }
    }

    override fun setTemperaturePreference(newPreference: TemperaturePreference): Flow<Unit> {
        return sharedPreferencesDataSource.setString(TEMPERATURE_KEY, newPreference.name)
    }

    override fun getAppThemePreference(): Flow<AppThemePreference> {
        return sharedPreferencesDataSource.getString(
            key = THEME_KEY,
            defaultValue = AppThemePreference.SYSTEM_DEFAULT.name
        ).map {
            when (it) {
                AppThemePreference.LIGHT.name -> AppThemePreference.LIGHT
                AppThemePreference.DARK.name -> AppThemePreference.DARK
                else -> AppThemePreference.SYSTEM_DEFAULT
            }
        }
    }

    override fun setAppThemePreference(theme: AppThemePreference): Flow<Unit> {
        return sharedPreferencesDataSource.setString(THEME_KEY, theme.name)
    }
}