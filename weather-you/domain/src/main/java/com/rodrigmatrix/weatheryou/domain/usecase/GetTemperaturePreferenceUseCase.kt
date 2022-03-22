package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetTemperaturePreferenceUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<TemperaturePreference> {
        return settingsRepository.getTemperaturePreference()
    }
}