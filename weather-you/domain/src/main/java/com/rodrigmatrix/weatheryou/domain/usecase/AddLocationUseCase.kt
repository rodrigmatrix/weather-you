package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

private const val MAX_LOCATIONS_KEY = "locations_limit"

class AddLocationUseCase(
    private val weatherRepository: WeatherRepository,
    private val getRemoteConfigLongUseCase: GetRemoteConfigLongUseCase,
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(
        name: String,
        latitude: Double,
        longitude: Double,
        countryCode: String,
    ): Flow<Unit> {
        return flow {
            val locationsCount = weatherRepository.getLocationsSize().first()
            val maxLocations = getRemoteConfigLongUseCase(MAX_LOCATIONS_KEY).toInt()
            val isPremium = settingsRepository.getIsPremiumUser().firstOrNull() == true
            if (!isPremium && (locationsCount >= maxLocations)) {
                throw LocationLimitException(maxLocations)
            } else{
                emitAll(weatherRepository.addLocation(name, latitude, longitude, countryCode))
            }
        }
    }
}