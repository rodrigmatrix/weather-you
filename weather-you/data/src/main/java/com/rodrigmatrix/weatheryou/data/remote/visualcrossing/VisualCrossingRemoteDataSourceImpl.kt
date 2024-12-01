package com.rodrigmatrix.weatheryou.data.remote.visualcrossing

import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

class VisualCrossingRemoteDataSourceImpl(
    private val visualCrossingService: VisualCrossingService,
    private val visualCrossingRemoteMapper: VisualCrossingRemoteMapper
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation> {
        return flow {
            emit(
                visualCrossingService.getWeatherWithCoordinates(
                    coordinates = "$latitude,$longitude",
                    unitGroup = "metric",
                )
            )
        }.map { visualCrossingRemoteMapper.map(it) }
    }

    override fun getWeather(name: String): Flow<WeatherLocation> {
        return flow {
            emit(
                visualCrossingService.getWeather(
                    location = name,
                    unitGroup = "metric"
                )
            )
        }.map { visualCrossingRemoteMapper.map(it) }
    }
}