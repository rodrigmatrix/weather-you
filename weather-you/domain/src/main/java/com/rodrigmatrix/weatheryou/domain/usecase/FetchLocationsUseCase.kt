package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class FetchLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(): Flow<List<WeatherLocation>> {
        return weatherRepository.fetchLocationsList()
    }
}