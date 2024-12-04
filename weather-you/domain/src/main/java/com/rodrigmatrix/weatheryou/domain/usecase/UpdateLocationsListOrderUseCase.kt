package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class UpdateLocationsListOrderUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(list: List<WeatherLocation>): Flow<Unit> {
        return weatherRepository.updateLocationsListOrder(
            list.filter { it.isCurrentLocation.not() }
                .mapIndexed { index, weatherLocation ->
                    weatherLocation.copy(orderIndex = index)
                }
        )
    }
}