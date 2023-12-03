package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class SetWidgetLocationUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherRepository.setSavedLocation(weatherLocation)
    }
}