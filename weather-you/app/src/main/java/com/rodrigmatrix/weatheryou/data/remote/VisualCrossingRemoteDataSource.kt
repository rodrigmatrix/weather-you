package com.rodrigmatrix.weatheryou.data.remote

import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import kotlinx.coroutines.flow.Flow

interface VisualCrossingRemoteDataSource {

    fun getWeather(location: String, unit: String): Flow<VisualCrossingWeatherResponse>
}