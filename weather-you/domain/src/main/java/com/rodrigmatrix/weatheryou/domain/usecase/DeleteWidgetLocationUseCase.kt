package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class DeleteWidgetLocationUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(): Flow<Unit> {
        return weatherRepository.deleteWidgetWeather()
    }
}