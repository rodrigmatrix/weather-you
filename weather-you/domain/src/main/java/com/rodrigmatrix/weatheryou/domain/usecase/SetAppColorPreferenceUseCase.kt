package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SetAppColorPreferenceUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(color: AppColorPreference): Flow<Unit> {
        return settingsRepository.setAppColorPreference(color)
    }
}