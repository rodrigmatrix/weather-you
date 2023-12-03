package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getAllLocations(): Flow<List<WeatherLocationEntity>>

    fun addLocation(location: WeatherLocationEntity): Flow<Unit>

    fun deleteLocation(id: Int): Flow<Unit>

    fun getSavedLocation(): Flow<WeatherLocationEntity?>

    fun getWidgetLocation(): Flow<WeatherWidgetLocationEntity?>

    fun getWidgetWeather(): Flow<WeatherWidgetLocationEntity?>

    fun saveWidgetLocation(location: WeatherWidgetLocationEntity): Flow<Unit>
}