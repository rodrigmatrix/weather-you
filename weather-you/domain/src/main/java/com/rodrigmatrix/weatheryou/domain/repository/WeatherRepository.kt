package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun addLocation(
        name: String,
        latitude: Double,
        longitude: Double,
        countryCode: String,
    ): Flow<Unit>

    fun getLocationsList(): Flow<List<WeatherLocation>>

    fun fetchLocationsList(forceUpdate: Boolean): Flow<Unit>

    fun fetchWidgetLocationsList(forceUpdate: Boolean): Flow<Unit>

    fun updateLocationsListOrder(list: List<WeatherLocation>): Flow<Unit>

    fun fetchLocation(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation>

    fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit>

    fun getLocalWeather(): Flow<WeatherLocation>

    fun getLocationsSize(): Flow<Int>

    fun getLocation(latitude: Double, longitude: Double): Flow<WeatherLocation?>

    fun setSavedLocation(weatherLocation: WeatherLocation, widgetId: String): Flow<Unit>

    fun deleteWidgetWeather(widgetId: String): Flow<Unit>

    fun getWidgetWeather(widgetId: String): Flow<WeatherLocation?>

    fun getWidgetLocationsSize(): Flow<Int>
}