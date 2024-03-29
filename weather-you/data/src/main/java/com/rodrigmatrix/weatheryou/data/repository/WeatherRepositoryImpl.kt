package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.core.extensions.catch
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.mapper.WeatherLocationDomainToEntityMapper
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.data.mapper.toWidgetWeather
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
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
    private val weatherLocationDomainToEntityMapper: WeatherLocationDomainToEntityMapper,
) : WeatherRepository {

    override fun addLocation(name: String, latitude: Double, longitude: Double): Flow<Unit> {
        return settingsRepository.getTemperaturePreference()
            .flatMapLatest { unit ->
                weatherYouRemoteDataSource.getWeather(latitude, longitude, unit)
            }
            .flatMapLatest { location ->
                val entity = weatherLocationDomainToEntityMapper.map(
                    location.copy(name = location.name.ifEmpty { name })
                )
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
                    .catch()
                    .firstOrNull()

                val fetchedLocations = weatherLocations.mapNotNull { weatherEntity ->
                    fetchLocation(weatherEntity.latitude, weatherEntity.longitude)
                        .catch()
                        .firstOrNull()?.copy(name = weatherEntity.name, id = weatherEntity.id)
                }.toMutableList()

                if (currentLocation!= null) {
                    val location = fetchLocation(currentLocation.latitude, currentLocation.longitude)
                        .catch { }
                        .firstOrNull()?.copy(
                            name = currentLocation.name,
                            isCurrentLocation = true,
                        )
                    location?.let { fetchedLocations.add(FIRST_INDEX, it) }
                }
                fetchedLocations
            }
    }

    override fun deleteLocation(id: Int): Flow<Unit> {
        return weatherLocalDataSource.deleteLocation(id)
    }

    override fun getLocalWeather(): Flow<WeatherLocation> {
        return userLocationDataSource.getCurrentLocation().flatMapLatest { currentLocation ->
            fetchLocation(currentLocation.latitude, currentLocation.longitude).map { location ->
                location.copy(name = currentLocation.name.ifEmpty { location.name })
            }
        }
    }

    override fun getLocationsSize(): Flow<Int> {
        return weatherLocalDataSource.getAllLocations().map { locationsList -> locationsList.size }
    }

    override fun updateWidgetWeather(): Flow<WidgetWeather?> {
        return getWidgetWeather().flatMapLatest { savedLocation ->
            if (savedLocation != null) {
                fetchLocation(
                    latitude = savedLocation.lat,
                    longitude = savedLocation.long
                ).mapLatest {
                    it.toWidgetWeather().copy(name = savedLocation.name)
                }
            } else {
                val gpsLocation = userLocationDataSource.getLastKnownLocation()
                    .firstOrNull()
                val firstSavedLocation = weatherLocalDataSource.getAllLocations()
                    .firstOrNull()?.firstOrNull()

                if (gpsLocation != null) {
                    fetchLocation(
                        latitude = gpsLocation.latitude,
                        longitude = gpsLocation.longitude,
                    ).mapLatest { it.toWidgetWeather().copy(name = gpsLocation.name) }
                } else if (firstSavedLocation != null) {
                    fetchLocation(
                        latitude = firstSavedLocation.latitude,
                        longitude = firstSavedLocation.longitude,
                    ).mapLatest { it.toWidgetWeather().copy(name = firstSavedLocation.name) }
                } else {
                    flowOf(null)
                }
            }
        }.onEach { widgetWeather ->
            if (widgetWeather != null) {
                weatherLocalDataSource.saveWidgetLocation(widgetWeather.toWeatherWidgetLocationEntity())
                    .firstOrNull()
            }
        }
    }

    override fun setSavedLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherLocalDataSource.saveWidgetLocation(
            weatherLocation.toWidgetWeather().toWeatherWidgetLocationEntity()
        )
    }

    override fun deleteWidgetWeather(): Flow<Unit> {
        return weatherLocalDataSource.deleteWidgetLocation()
    }

    override fun getWidgetWeather(): Flow<WidgetWeather?> {
        return weatherLocalDataSource.getWidgetWeather().map { weatherWidget ->
            weatherWidget?.toWidgetWeather()
        }
    }
}