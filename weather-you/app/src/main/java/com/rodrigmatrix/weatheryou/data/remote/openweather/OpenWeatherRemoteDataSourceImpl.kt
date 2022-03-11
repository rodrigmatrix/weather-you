package com.rodrigmatrix.weatheryou.data.remote.openweather

import com.rodrigmatrix.weatheryou.data.mapper.OpenWeatherRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

class OpenWeatherRemoteDataSourceImpl(
    private val openWeatherService: OpenWeatherService,
    private val openWeatherRemoteMapper: OpenWeatherRemoteMapper
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double
    ): Flow<WeatherLocation> {
        return flow {
            emit(
                openWeatherService.getWeather(
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    unit = getUnit(),
                    language = Locale.getDefault().toString()
                )
            )
        }.map(openWeatherRemoteMapper::map)
    }

    private fun getUnit(): String {
        return when (Locale.getDefault().language) {
            Locale.UK.language -> "metric"
            Locale.US.language -> "metric"
            else -> "metric"
        }
    }
}