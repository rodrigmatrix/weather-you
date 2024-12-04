package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(): Flow<List<WeatherLocation>> {
        return weatherRepository.getLocationsList().map {
            it.sortedBy { it.isCurrentLocation }
                .sortedBy { it.orderIndex }
        }
    }
}