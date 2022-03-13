package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class FetchLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(latitude: Double, longitude: Double): Flow<WeatherLocation> {
        return weatherRepository.fetchLocation(latitude, longitude)
    }
}