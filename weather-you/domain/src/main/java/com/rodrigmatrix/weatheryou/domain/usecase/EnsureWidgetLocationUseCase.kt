package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class EnsureWidgetLocationUseCase(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(widgetId: String): Flow<Unit> = weatherRepository.ensureWidgetLocation(widgetId)
}
