package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SetAppThemePreferenceUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(theme: AppThemePreference): Flow<Unit> {
        return settingsRepository.setAppThemePreference(theme)
    }
}
