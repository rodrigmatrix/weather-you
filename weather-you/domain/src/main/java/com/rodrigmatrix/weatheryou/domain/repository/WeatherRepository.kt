package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.model.WidgetWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun addLocation(name: String, latitude: Double, longitude: Double): Flow<Unit>

    fun fetchLocationsList(): Flow<List<WeatherLocation>>

    fun fetchLocation(latitude: Double, longitude: Double): Flow<WeatherLocation>

    fun deleteLocation(id: Int): Flow<Unit>

    fun getLocalWeather(): Flow<WeatherLocation>

    fun getLocationsSize(): Flow<Int>

    fun updateWidgetWeather(): Flow<WidgetWeather?>

    fun setSavedLocation(weatherLocation: WeatherLocation): Flow<Unit>

    fun deleteWidgetWeather(): Flow<Unit>

    fun getWidgetWeather(): Flow<WidgetWeather?>
}