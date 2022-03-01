package com.rodrigmatrix.weatheryou.data.remote.openweather

import com.rodrigmatrix.weatheryou.data.mapper.OpenWeatherRemoteMapper
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.OpenWeatherService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OpenWeatherRemoteDataSourceImpl(
    private val openWeatherService: OpenWeatherService,
    private val openWeatherRemoteMapper: OpenWeatherRemoteMapper
) : WeatherYouRemoteDataSource {

    override fun getWeather(location: String, unit: String): Flow<WeatherLocation> {
        return flow {
            emit(openWeatherService.getWeather(location = location))
        }.map(openWeatherRemoteMapper::map)
    }
}