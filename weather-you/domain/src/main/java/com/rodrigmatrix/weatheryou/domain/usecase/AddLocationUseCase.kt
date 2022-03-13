package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class AddLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(
        name: String,
        latitude: Double,
        longitude: Double
    ): Flow<Unit> {
        return weatherRepository.addLocation(name, latitude, longitude)
    }
}