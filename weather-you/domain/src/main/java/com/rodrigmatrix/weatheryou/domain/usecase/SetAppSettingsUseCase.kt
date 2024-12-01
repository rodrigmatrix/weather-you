package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SetAppSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(appSettings: AppSettings): Flow<Unit> {
        return settingsRepository.setAppSettings(appSettings)
    }
}