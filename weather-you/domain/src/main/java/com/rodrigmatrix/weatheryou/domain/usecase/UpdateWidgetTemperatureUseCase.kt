package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class UpdateWidgetTemperatureUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(): Flow<WidgetWeather?> =
        weatherRepository.updateWidgetWeather()
}