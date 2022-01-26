package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun addLocation(locationName: String): Flow<WeatherLocation>

    fun getLocation(resolvedAddress: String): Flow<WeatherLocation>

    fun getLocationsList(): Flow<List<WeatherLocation>>

    fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit>
}