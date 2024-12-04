package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getAppSettings(): Flow<AppSettings>

    fun setAppSettings(settings: AppSettings): Flow<Unit>
}