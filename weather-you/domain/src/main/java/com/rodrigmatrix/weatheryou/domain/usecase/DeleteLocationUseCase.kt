package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class DeleteLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherRepository.deleteLocation(weatherLocation)
    }
}