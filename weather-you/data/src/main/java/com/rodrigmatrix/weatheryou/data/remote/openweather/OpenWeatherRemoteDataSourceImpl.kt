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

private const val METRIC = "metric"
private const val IMPERIAL = "imperial"
private const val STANDARD = "standard"

class OpenWeatherRemoteDataSourceImpl(
    private val openWeatherService: OpenWeatherService,
    private val openWeatherRemoteMapper: OpenWeatherRemoteMapper,
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        unit: TemperaturePreference
    ): Flow<WeatherLocation> {
        return flow {
            emit(
                openWeatherService.getWeather(
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    unit = getUnit(unit),
                    language = Locale.getDefault().toString(),
                )
            )
        }.map { openWeatherRemoteMapper.map(it, unit) }
    }

    override fun getWeather(name: String, unit: TemperaturePreference): Flow<WeatherLocation> {
        return flow {
            emit(
                openWeatherService.getWeather(
                    location = name,
                    unit = getUnit(unit),
                    language = Locale.getDefault().toString(),
                )
            )
        }.map { openWeatherRemoteMapper.map(it, unit) }
    }

    private fun getUnit(unit: TemperaturePreference): String {
        return when (unit) {
            TemperaturePreference.METRIC -> METRIC
            TemperaturePreference.IMPERIAL -> IMPERIAL
            else -> STANDARD
        }
    }
}