package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    fun getAppSettings(): Flow<AppSettings>

    fun setAppSettings(settings: AppSettings): Flow<Unit>

    fun getIsPremiumUser(): Flow<Boolean>

    fun setIsPremiumUser(premium: Boolean): Flow<Unit>

    fun getSupportPromptDismissedAtMillis(): Flow<Long?>

    fun setSupportPromptDismissedAtMillis(timestampMillis: Long): Flow<Unit>

    fun getHasSeenUsableForecast(): Flow<Boolean>

    fun setHasSeenUsableForecast(): Flow<Unit>
}
