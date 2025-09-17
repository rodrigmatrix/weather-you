package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(id: Int, isCurrentLocation: Boolean): Flow<WeatherLocation> {
        return weatherRepository.getLocation(id, isCurrentLocation)
    }
}