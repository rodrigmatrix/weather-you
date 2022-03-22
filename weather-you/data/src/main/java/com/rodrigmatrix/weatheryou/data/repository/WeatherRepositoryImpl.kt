package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.mapper.WeatherLocationDomainToEntityMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImpl(
    private val weatherYouRemoteDataSource: WeatherYouRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val settingsRepository: SettingsRepository,
    private val weatherLocationDomainToEntityMapper: WeatherLocationDomainToEntityMapper
) : WeatherRepository {

    override fun addLocation(name: String, latitude: Double, longitude: Double): Flow<Unit> {
        return settingsRepository.getTemperaturePreference()
            .flatMapLatest { unit ->
                weatherYouRemoteDataSource.getWeather(latitude, longitude, unit)
            }
            .flatMapLatest { location ->
                val entity = weatherLocationDomainToEntityMapper.map(location.copy(name = name))
                weatherLocalDataSource.addLocation(entity)
            }
    }

    override fun fetchLocation(latitude: Double, longitude: Double): Flow<WeatherLocation> {
        return settingsRepository.getTemperaturePreference()
            .flatMapLatest { unit ->
                weatherYouRemoteDataSource.getWeather(latitude, longitude, unit)
            }
    }

    override fun fetchLocationsList(): Flow<List<WeatherLocation>> {
        return weatherLocalDataSource
            .getAllLocations()
            .map { weatherLocations ->
                weatherLocations.mapNotNull {
                    fetchLocation(it.latitude, it.longitude)
                        .catch { emitAll(flowOf()) }
                        .firstOrNull()?.copy(name = it.name)
                }
            }
    }

    override fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherLocalDataSource.deleteLocation(weatherLocation.latitude, weatherLocation.longitude)
    }

    override fun getLocalWeather(): Flow<WeatherLocation> {
        return weatherLocalDataSource.getLocalWeather().flatMapLatest { locationEntity ->
            fetchLocation(locationEntity.latitude, locationEntity.longitude).map { location ->
                location.copy(name = locationEntity.name)
            }
        }
    }
}