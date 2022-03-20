package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getLocalWeather(): Flow<WeatherLocationEntity>

    fun getAllLocations(): Flow<List<WeatherLocationEntity>>

    fun addLocation(location: WeatherLocationEntity): Flow<Unit>

    fun deleteLocation(latitude: Double, longitude: Double): Flow<Unit>
}