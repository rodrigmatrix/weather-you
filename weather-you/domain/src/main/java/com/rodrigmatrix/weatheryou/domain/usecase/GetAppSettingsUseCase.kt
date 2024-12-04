package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetAppSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<AppSettings> {
        return settingsRepository.getAppSettings()
    }
}