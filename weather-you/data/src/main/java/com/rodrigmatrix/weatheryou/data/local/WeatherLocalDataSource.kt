package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.model.CurrentLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getAllLocations(): Flow<List<WeatherLocationEntity>>

    fun upsertLocation(location: WeatherLocationEntity): Flow<Unit>

    fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit>

    fun getCurrentLocation(): Flow<CurrentLocationEntity?>

    fun upsertCurrentLocation(currentLocation: CurrentLocationEntity): Flow<Unit>

    fun upsertCurrentWeather(weatherEntity: WeatherEntity): Flow<Unit>

    fun getWidgetLocation(widgetId: String): Flow<WeatherWidgetLocationEntity?>

    fun getWeather(latitude: Double, longitude: Double): Flow<WeatherEntity?>

    fun getCurrentLocationWeather(): Flow<WeatherEntity?>

    fun upsertWeather(weatherEntity: WeatherEntity): Flow<Unit>

    fun getWidgetWeatherList(): Flow<List<WeatherWidgetLocationEntity>>

    fun saveWidgetLocation(location: WeatherWidgetLocationEntity): Flow<Unit>

    fun deleteWidgetLocation(widgetId: String): Flow<Unit>
}