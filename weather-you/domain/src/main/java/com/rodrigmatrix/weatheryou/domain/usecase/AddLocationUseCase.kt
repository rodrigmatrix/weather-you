package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AddLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(
        name: String,
        latitude: Double,
        longitude: Double
    ): Flow<Unit> {
        return flow {
            val locationsCount = weatherRepository.getLocationsSize().first()
            if (locationsCount > 5) {
                throw LocationLimitException(5)
            } else{
                emitAll(weatherRepository.addLocation(name, latitude, longitude))
            }
        }
    }
}