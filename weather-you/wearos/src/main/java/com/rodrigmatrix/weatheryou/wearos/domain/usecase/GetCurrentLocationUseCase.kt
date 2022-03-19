package com.rodrigmatrix.weatheryou.wearos.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(): Flow<WeatherLocation> {
        return weatherRepository.getLocalWeather()
    }
}