package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.SettingsLocalDataSource
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override fun getAppSettings(): Flow<AppSettings> {
        return settingsLocalDataSource.getAppSettings()
    }

    override fun setAppSettings(settings: AppSettings): Flow<Unit> {
        return settingsLocalDataSource.setAppSettings(settings)
    }

    override fun getIsPremiumUser(): Flow<Boolean> {
        return settingsLocalDataSource.getIsPremiumUser()
    }

    override fun setIsPremiumUser(premium: Boolean): Flow<Unit> {
        return settingsLocalDataSource.setIsPremiumUser(premium)
    }
}