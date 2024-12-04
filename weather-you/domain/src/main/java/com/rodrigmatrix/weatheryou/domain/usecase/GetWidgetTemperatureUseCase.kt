package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWidgetTemperatureUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(widgetId: String): Flow<WeatherLocation?> =
        weatherRepository.getWidgetWeather(widgetId)
}