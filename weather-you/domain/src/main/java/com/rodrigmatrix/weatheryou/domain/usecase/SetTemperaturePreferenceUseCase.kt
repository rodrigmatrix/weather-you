package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SetTemperaturePreferenceUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(newPreference: TemperaturePreference): Flow<Unit> {
        return settingsRepository.setTemperaturePreference(newPreference)
    }
}