package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetAppThemePreferenceUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<AppThemePreference> {
        return settingsRepository.getAppThemePreference()
    }
}
