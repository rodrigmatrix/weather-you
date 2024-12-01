package com.rodrigmatrix.weatheryou.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.rodrigmatrix.weatheryou.core.extensions.catch
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.mapper.getCurrentTime
import com.rodrigmatrix.weatheryou.data.mapper.toDomain
import com.rodrigmatrix.weatheryou.data.mapper.toEntity
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherEntity
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherLocation
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import org.joda.time.Minutes

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImpl(
    private val weatherYouRemoteDataSource: WeatherYouRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val userLocationDataSource: UserLocationDataSource,
    private val searchRepository: SearchRepository,
    private val applicationContext: Context,
) : WeatherRepository {

    override fun addLocation(
        name: String,
        latitude: Double,
        longitude: Double,
        countryCode: String,
    ): Flow<Unit> {
        return searchRepository.getTimezone(latitude, longitude)
            .flatMapLatest { timezone ->
                val weatherEntity = WeatherLocationEntity(
                    latitude = latitude,
                    orderIndex = getLocationsSize().firstOrNull() ?: 0,
                    longitude = longitude,
                    name = name,
                    countryCode = countryCode,
                    timeZone = timezone,
                )
                getOrUpdateLocation(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    countryCode = countryCode,
                    timeZone = timezone,
                    forceUpdate = true,
                )
                weatherLocalDataSource.upsertLocation(weatherEntity)
            }
    }

    override fun fetchLocation(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation> {
        return weatherYouRemoteDataSource.getWeather(latitude, longitude, countryCode, timezone)
    }

    override fun updateLocationsListOrder(list: List<WeatherLocation>): Flow<Unit> {
        return flow {
            list.forEach { weatherLocation ->
                weatherLocalDataSource.upsertLocation(weatherLocation.toWeatherLocationEntity())
                    .firstOrNull()
            }
            emit(Unit)
        }
    }

    override fun fetchLocationsList(forceUpdate: Boolean): Flow<Unit> {
        return weatherLocalDataSource.getAllLocations()
            .map { weatherLocations ->
                getOrUpdateCurrentLocation(forceUpdate)

                val fetchedLocations = weatherLocations.mapNotNull { weatherEntity ->
                    getOrUpdateLocation(
                        name = weatherEntity.name,
                        latitude = weatherEntity.latitude,
                        longitude = weatherEntity.longitude,
                        countryCode = weatherEntity.countryCode,
                        timeZone = weatherEntity.timeZone,
                        forceUpdate = forceUpdate,
                    )?.copy(
                        id = weatherEntity.id,
                        name = weatherEntity.name,
                        orderIndex = weatherEntity.orderIndex
                    )
                }.toMutableList()

                fetchedLocations
                    .filter { it.isCurrentLocation.not() }
                    .forEach {
                        weatherLocalDataSource.upsertWeather(it.toWeatherEntity()).firstOrNull()
                    }
            }
    }

    override fun fetchWidgetLocationsList(forceUpdate: Boolean): Flow<Unit> {
        return flowOf(Unit)
    }

    override fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        return weatherLocalDataSource.deleteLocation(weatherLocation)
    }

    override fun getLocalWeather(): Flow<WeatherLocation> {
        return userLocationDataSource.getCurrentLocation().flatMapLatest { currentLocation ->
            fetchLocation(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude,
                countryCode = currentLocation.countryCode,
                timezone = currentLocation.timezone,
            ).map { location ->
                location.copy(name = currentLocation.name.ifEmpty { location.name })
            }
        }
    }

    override fun getLocationsSize(): Flow<Int> {
        return weatherLocalDataSource.getAllLocations().map { locationsList -> locationsList.size }
    }

    override fun setSavedLocation(weatherLocation: WeatherLocation, widgetId: String): Flow<Unit> {
        return weatherLocalDataSource.saveWidgetLocation(
            weatherLocation.toWeatherWidgetLocationEntity().copy(id = widgetId)
        )
    }

    override fun deleteWidgetWeather(widgetId: String): Flow<Unit> {
        return weatherLocalDataSource.deleteWidgetLocation(widgetId)
    }

    override fun getWidgetWeather(widgetId: String): Flow<WeatherLocation?> {
        return weatherLocalDataSource.getWidgetLocation(widgetId).map { weatherWidget ->
            weatherWidget?.let {
                if (weatherWidget.isCurrentLocation) {
                    getWidgetCurrentLocation(
                        widgetId = weatherWidget.id,
                        forceUpdate = false,
                    )
                } else {
                    getOrUpdateLocation(
                        name = weatherWidget.name,
                        latitude = weatherWidget.latitude,
                        longitude = weatherWidget.longitude,
                        countryCode = weatherWidget.countryCode,
                        timeZone = weatherWidget.timeZone,
                        forceUpdate = false,
                    )?.copy(
                        widgetId = weatherWidget.id,
                        name = weatherWidget.name,
                    )
                }
            } ?: getWidgetCurrentLocation(
                widgetId = widgetId,
                forceUpdate = true,
            )
        }
    }

    override fun getLocationsList(): Flow<List<WeatherLocation>> {
        return combine(
            weatherLocalDataSource.getAllLocations(),
            weatherLocalDataSource.getCurrentLocation()
        ) { locationsList, currentLocation ->
            locationsList to currentLocation
        }.flatMapLatest { pair ->
            val locations = pair.first.toMutableList()
            var currentLocation: Flow<WeatherLocation>? = null
            if (pair.second != null) {
                currentLocation = weatherLocalDataSource.getCurrentLocationWeather().mapNotNull {
                    it?.toWeatherLocation(id= -1, orderIndex = -1)
                }
            }
            combine(
                locations.map { location ->
                    weatherLocalDataSource.getWeather(location.latitude, location.longitude).mapNotNull {
                        it?.toWeatherLocation(id = location.id, orderIndex = location.orderIndex)
                    }
                } + if (currentLocation != null) listOf(currentLocation) else listOf()
            ) {
                it.asList()
            }
        }
    }

    private suspend fun getCurrentLocation(
        forceUpdate: Boolean,
        hasLocationPermission: Boolean,
    ): CurrentLocation? {
        val currentLocationEntity = weatherLocalDataSource.getCurrentLocation()
            .firstOrNull()
            ?.toDomain()
        val minutesBetween = Minutes.minutesBetween(
            currentLocationEntity?.lastUpdate ?: DateTime.now(),
            DateTime.now(),
        )
        return if (hasLocationPermission && (forceUpdate || minutesBetween.minutes > 60 || currentLocationEntity == null)) {
            try {
                userLocationDataSource.getCurrentLocation()
                    .catch().firstOrNull() ?:
                userLocationDataSource.getLastKnownLocation().catch().firstOrNull()
            } catch (_: Exception) {
                currentLocationEntity
            }
        } else {
            currentLocationEntity
        }
    }

    private suspend fun getOrUpdateLocation(
        name: String,
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timeZone: String,
        forceUpdate: Boolean,
    ): WeatherLocation? {
        val location = weatherLocalDataSource.getWeather(
            latitude,
            longitude,
        ).firstOrNull()?.toWeatherLocation(
            id = -1,
            orderIndex = -1
        )?.copy(name = name)
        return if (forceUpdate || location == null || location.expirationDate.isBefore(location.timeZone.getCurrentTime())) {
            fetchLocation(
                latitude = latitude,
                longitude = longitude,
                countryCode = countryCode,
                timezone = timeZone,
            )
                .catch()
                .firstOrNull()
                ?.copy(name = name)?.also {
                    weatherLocalDataSource.upsertWeather(it.toWeatherEntity())
                        .firstOrNull()
                }
        } else {
            location
        }
    }

    private suspend fun getOrUpdateCurrentLocation(
        forceUpdate: Boolean,
        hasLocationPermission: Boolean = true,
    ): WeatherLocation? {
        val currentLocation = getCurrentLocation(
            forceUpdate = forceUpdate,
            hasLocationPermission = hasLocationPermission,
        ) ?: return null
        val currentLocationData = weatherLocalDataSource.getCurrentLocationWeather()
            .firstOrNull()?.toWeatherLocation(
                id = -1,
                orderIndex = -1,
            )
        weatherLocalDataSource.upsertCurrentLocation(currentLocation.toEntity())
            .firstOrNull()
        if (forceUpdate || currentLocationData == null || currentLocationData.expirationDate.isBefore(currentLocationData.timeZone.getCurrentTime())) {
            val location = fetchLocation(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude,
                countryCode = currentLocation.countryCode,
                timezone = currentLocation.timezone,
            )
                .catch()
                .firstOrNull()?.copy(
                    id = -1,
                    orderIndex = -1,
                    countryCode = currentLocation.countryCode,
                    name = currentLocation.name,
                    isCurrentLocation = true,
                )?.also {
                    weatherLocalDataSource.upsertCurrentWeather(it.toWeatherEntity())
                        .firstOrNull()
                }
            return location ?: currentLocationData
        } else {
            return currentLocationData
        }
    }

    private suspend fun getWidgetCurrentLocation(
        widgetId: String,
        forceUpdate: Boolean,
    ): WeatherLocation? {
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            getOrUpdateCurrentLocation(
                forceUpdate = forceUpdate,
                hasLocationPermission = hasLocationPermission,
            )?.copy(
                widgetId = widgetId,
            )
        } else {
            getOrUpdateCurrentLocation(
                forceUpdate = forceUpdate,
                hasLocationPermission = true,
            )?.copy(
                widgetId = widgetId,
            )
        }
    }
}