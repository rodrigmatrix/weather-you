package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun addLocation(locationName: String): Flow<Unit>

    fun fetchLocation(resolvedAddress: String): Flow<WeatherLocation>

    fun fetchLocationsList(): Flow<List<WeatherLocation>>

    fun deleteLocation(locationName: String): Flow<Unit>
}