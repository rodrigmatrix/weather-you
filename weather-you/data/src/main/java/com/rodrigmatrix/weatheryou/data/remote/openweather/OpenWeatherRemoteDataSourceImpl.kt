package com.rodrigmatrix.weatheryou.data.remote.openweather

import com.rodrigmatrix.weatheryou.data.mapper.OpenWeatherRemoteMapper
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

class OpenWeatherRemoteDataSourceImpl(
    private val openWeatherService: OpenWeatherService,
    private val openWeatherRemoteMapper: OpenWeatherRemoteMapper,
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation> {
        return flow {
            emit(
                openWeatherService.getWeather(
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    unit = "metric",
                    language = Locale.getDefault().toString(),
                )
            )
        }.map { openWeatherRemoteMapper.map(it) }
    }

    override fun getWeather(name: String): Flow<WeatherLocation> {
        return flow {
            emit(
                openWeatherService.getWeather(
                    location = name,
                    unit = "metric",
                    language = Locale.getDefault().toString(),
                )
            )
        }.map { openWeatherRemoteMapper.map(it) }
    }
}