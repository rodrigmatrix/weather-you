package com.rodrigmatrix.weatheryou.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
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
import kotlinx.coroutines.delay
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
    private val firebaseAnalytics: FirebaseAnalytics,
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
                    countryCode = countryCode.uppercase(),
                    timeZone = timezone,
                )
                getOrUpdateLocation(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    countryCode = countryCode.uppercase(),
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
                var currentLocationUpdated = false
                var retryCount = 0
                val maxRetries = 3
                
                while (!currentLocationUpdated && retryCount < maxRetries) {
                    try {
                        getOrUpdateCurrentLocation(
                            forceUpdate = forceUpdate,
                            hasLocationPermission = hasLocationPermission()
                        )
                        currentLocationUpdated = true
                    } catch (e: Exception) {
                        retryCount++
                        if (retryCount < maxRetries) {
                            delay(1000L * retryCount)
                        } else {
                            // Log the final failure
                            firebaseAnalytics.logEvent("CURRENT_LOCATION_RETRY_FAILED", bundleOf(
                                "retry_count" to retryCount,
                                "error" to e.localizedMessage
                            ))
                        }
                    }
                }

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

    override fun getLocation(latitude: Double, longitude: Double): Flow<WeatherLocation?> {
        return weatherLocalDataSource.getWeather(latitude, longitude).flatMapLatest { weather ->
            require(weather != null) {
                throw IllegalArgumentException("weather not found")
            }
            if (weather.isCurrentLocation) {
                weatherLocalDataSource.getCurrentLocation().map { currentLocation ->
                    require(currentLocation != null) {
                        throw IllegalArgumentException("CurrentLocation not found")
                    }
                    weather.toWeatherLocation(
                        id = currentLocation.id,
                        orderIndex = -1,
                    )
                }
            } else {
                weatherLocalDataSource.getLocation(latitude = latitude, longitude = longitude).map { location ->
                    require(location != null) {
                        throw IllegalArgumentException("Location not found")
                    }
                    weather.toWeatherLocation(
                        id = location.id,
                        orderIndex = -1,
                    )
                }
            }
        }
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
        return weatherLocalDataSource.getWidgetLocation(widgetId)
            .map { weatherWidget ->
                weatherWidget?.let {
                    if (weatherWidget.isCurrentLocation) {
                        getWidgetCurrentLocation(
                            widgetId = widgetId,
                            forceUpdate = true,
                        )
                    } else {
                        getOrUpdateLocation(
                            name = weatherWidget.name,
                            latitude = weatherWidget.latitude,
                            longitude = weatherWidget.longitude,
                            countryCode = weatherWidget.countryCode,
                            timeZone = weatherWidget.timeZone,
                            forceUpdate = true,
                            widgetId = widgetId,
                        )?.copy(
                            widgetId = widgetId,
                            name = weatherWidget.name,
                        )
                    }
                } ?: if (hasLocationPermission()) {
                    getWidgetCurrentLocation(
                        widgetId = widgetId,
                        forceUpdate = true,
                    )?.also {
                        setSavedLocation(it, widgetId)
                    }
                } else {
                    getLocationsList().firstOrNull()?.firstOrNull()?.let { location ->
                        getOrUpdateLocation(
                            name = location.name,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            countryCode = location.countryCode,
                            timeZone = location.timeZone,
                            forceUpdate = true,
                            widgetId = widgetId,
                        )?.copy(
                            widgetId = widgetId,
                            name = location.name,
                        )?.also {
                            setSavedLocation(it, widgetId)
                        }
                    }
                }
        }
    }

    override fun getLocationsList(): Flow<List<WeatherLocation>> {
        return combine(
            weatherLocalDataSource.getAllLocations(),
            weatherLocalDataSource.getCurrentLocation()
        ) { locationsList, currentLocationInfo ->
            locationsList to currentLocationInfo
        }.flatMapLatest { pair ->
            val locations = pair.first.toMutableList()
            var currentLocationFlow: Flow<WeatherLocation>? = null
            if (pair.second != null) {
                currentLocationFlow = weatherLocalDataSource.getCurrentLocationWeather().mapNotNull {
                    it?.toWeatherLocation(id = -1, orderIndex = -1)
                }
            }
            val locationDetailFlows = locations.map { location ->
                weatherLocalDataSource.getWeather(location.latitude, location.longitude).mapNotNull {
                    it?.toWeatherLocation(id = location.id, orderIndex = location.orderIndex)
                }
            } + if (currentLocationFlow != null) listOf(currentLocationFlow) else emptyList()

            if (locationDetailFlows.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(locationDetailFlows) {
                    it.asList()
                }
            }
        }
    }

    override fun getLocation(id: Int, isCurrentLocation: Boolean): Flow<WeatherLocation> {
        return if (isCurrentLocation) {
            weatherLocalDataSource.getCurrentLocation()
                .flatMapLatest { locationEntity ->
                    weatherLocalDataSource.getCurrentLocationWeather()
                        .map { weather ->
                            weather?.toWeatherLocation(
                                id = 0,
                                orderIndex = 0,
                            ) ?: throw Exception("Weather not found")
                        }
                }
        } else {
            weatherLocalDataSource.getLocation(id = id)
                .flatMapLatest { locationEntity ->
                    weatherLocalDataSource.getWeather(latitude = locationEntity.latitude, longitude = locationEntity.longitude)
                        .map { weather ->
                            weather?.toWeatherLocation(
                                id = locationEntity.id,
                                orderIndex = locationEntity.orderIndex,
                            ) ?: throw Exception("Weather not found")
                        }
                }
        }
    }

    override fun getWidgetLocationsSize(): Flow<Int> {
        return weatherLocalDataSource.getWidgetWeatherList().map { it.size }
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
            userLocationDataSource.getCurrentLocation()
                .firstOrNull() ?: try {
                    userLocationDataSource.getLastKnownLocation().firstOrNull()
                } catch (e: Exception) {
                    currentLocationEntity?.also {
                        firebaseAnalytics.logEvent("LOCATION_SERVICES_ERROR", bundleOf(
                            "error" to "Both current and last known location failed",
                            "last_known_error" to e.localizedMessage
                        ))
                    }
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
        widgetId: String = "",
    ): WeatherLocation? {
        val location = weatherLocalDataSource.getWeather(
            latitude,
            longitude,
        ).firstOrNull()?.toWeatherLocation(
            id = -1,
            orderIndex = -1,
            widgetId = widgetId,
        )?.copy(name = name)
        return if (forceUpdate || location == null || location.expirationDate.isBefore(location.timeZone.getCurrentTime())) {
            fetchLocation(
                latitude = latitude,
                longitude = longitude,
                countryCode = countryCode,
                timezone = timeZone,
            ).map {
                Result.success<WeatherLocation?>(it)
            }.catch {
                firebaseAnalytics.logEvent("FETCH_LOCATION_ERROR", bundleOf("error" to it.localizedMessage))
                emit(Result.success(location))
            }.map {
                it.getOrNull()
            }
                .firstOrNull()
                ?.copy(
                    name = name,
                    widgetId = widgetId,
                )?.also {
                    weatherLocalDataSource.upsertWeather(it.toWeatherEntity())
                        .firstOrNull()
                }
        } else {
            location
        }
    }

    private suspend fun getOrUpdateCurrentLocation(
        forceUpdate: Boolean,
        widgetId: String = "",
        hasLocationPermission: Boolean,
    ): WeatherLocation? {
        val currentLocation = getCurrentLocation(
            forceUpdate = forceUpdate,
            hasLocationPermission = hasLocationPermission,
        )

        if (currentLocation == null) {
            firebaseAnalytics.logEvent("CURRENT_LOCATION_NULL", bundleOf(
                "force_update" to forceUpdate,
                "has_location_permission" to hasLocationPermission
            ))
            return null
        }
        
        val currentLocationData = weatherLocalDataSource.getCurrentLocationWeather()
            .firstOrNull()?.toWeatherLocation(
                id = -1,
                orderIndex = -1,
                widgetId = widgetId,
            )
        weatherLocalDataSource.upsertCurrentLocation(currentLocation.toEntity())
            .firstOrNull()
        if (forceUpdate || currentLocationData == null || currentLocationData.expirationDate.isBefore(currentLocationData.timeZone.getCurrentTime())) {
            val location = fetchLocation(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude,
                countryCode = currentLocation.countryCode,
                timezone = currentLocation.timezone,
            ).map {
                Result.success<WeatherLocation?>(it)
            }.catch {
                firebaseAnalytics.logEvent("FETCH_LOCATION_ERROR", bundleOf("error" to it.localizedMessage))
                emit(Result.success(currentLocationData))
            }.map {
                it.getOrNull()
            }.firstOrNull()?.copy(
                    id = -1,
                    orderIndex = -1,
                    countryCode = currentLocation.countryCode,
                    name = currentLocation.name,
                    isCurrentLocation = true,
                    widgetId = widgetId,
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
        return getOrUpdateCurrentLocation(
            forceUpdate = forceUpdate,
            widgetId = widgetId,
            hasLocationPermission = hasBackgroundLocationPermission(),
        )
    }

    private fun hasBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}