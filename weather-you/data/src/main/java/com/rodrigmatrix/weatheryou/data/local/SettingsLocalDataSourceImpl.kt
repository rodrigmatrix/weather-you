package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TEMPERATURE_KEY = "temperature_pref"

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
}