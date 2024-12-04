package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class UpdateWidgetLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(forceUpdate: Boolean = false): Flow<Unit> {
        return weatherRepository.fetchWidgetLocationsList(forceUpdate)
    }
}