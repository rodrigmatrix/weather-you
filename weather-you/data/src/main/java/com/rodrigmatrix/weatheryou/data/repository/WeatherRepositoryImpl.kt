package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.mapper.WeatherLocationDomainToEntityMapper
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

private const val FIRST_INDEX = 0

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImpl(
    private val weatherYouRemoteDataSource: WeatherYouRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val userLocationDataSource: UserLocationDataSource,
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
        return weatherLocalDataSource.getAllLocations()
            .map { weatherLocations ->
                val currentLocation = userLocationDataSource.getLastKnownLocation()
                    .catch {  }
                    .firstOrNull()

                val fetchedLocations = weatherLocations.mapNotNull {
                    fetchLocation(it.latitude, it.longitude)
                        .catch {  }
                        .firstOrNull()?.copy(name = it.name)
                }.toMutableList()

                if (currentLocation!= null) {
                    val location = fetchLocation(currentLocation.latitude, currentLocation.longitude)
                        .catch { }
                        .firstOrNull()?.copy(name = currentLocation.name, isCurrentLocation = true)
                    location?.let { fetchedLocations.add(FIRST_INDEX, it) }
                }
                fetchedLocations
            }
    }

    override fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherLocalDataSource.deleteLocation(weatherLocation.latitude, weatherLocation.longitude)
    }

    override fun getLocalWeather(): Flow<WeatherLocation> {
        return userLocationDataSource.getLastKnownLocation().flatMapLatest { locationEntity ->
            fetchLocation(locationEntity.latitude, locationEntity.longitude).map { location ->
                location.copy(name = locationEntity.name)
            }
        }
    }
}