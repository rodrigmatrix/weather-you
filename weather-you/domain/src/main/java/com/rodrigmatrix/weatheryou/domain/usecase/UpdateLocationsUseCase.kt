package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class UpdateLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(
        forceUpdate: Boolean = false,
        forceUpdateLocation: WeatherLocation? = null,
    ): Flow<Unit> {
        return weatherRepository.fetchLocationsList(forceUpdate, forceUpdateLocation)
    }
}
