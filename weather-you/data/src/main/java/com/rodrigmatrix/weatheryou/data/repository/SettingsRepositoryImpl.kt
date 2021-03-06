package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.SettingsLocalDataSource
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override fun getTemperaturePreference(): Flow<TemperaturePreference> {
        return settingsLocalDataSource.getTemperaturePreference()
    }

    override fun setTemperaturePreference(newPreference: TemperaturePreference): Flow<Unit> {
        return settingsLocalDataSource.setTemperaturePreference(newPreference)
    }

    override fun getAppThemePreference(): Flow<AppThemePreference> {
        return settingsLocalDataSource.getAppThemePreference()
    }

    override fun setAppThemePreference(theme: AppThemePreference): Flow<Unit> {
        return settingsLocalDataSource.setAppThemePreference(theme)
    }
}