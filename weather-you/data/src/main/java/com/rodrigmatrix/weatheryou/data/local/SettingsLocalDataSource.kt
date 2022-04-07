package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    fun getTemperaturePreference(): Flow<TemperaturePreference>

    fun setTemperaturePreference(newPreference: TemperaturePreference): Flow<Unit>

    fun getAppThemePreference(): Flow<AppThemePreference>

    fun setAppThemePreference(theme: AppThemePreference): Flow<Unit>
}