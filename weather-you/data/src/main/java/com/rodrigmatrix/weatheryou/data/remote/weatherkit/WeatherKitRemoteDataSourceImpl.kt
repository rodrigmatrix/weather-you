package com.rodrigmatrix.weatheryou.data.remote.weatherkit

import com.rodrigmatrix.weatheryou.data.mapper.WeatherKitRemoteMapper
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.WeatherKitService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class WeatherKitRemoteDataSourceImpl(
    private val weatherKitService: WeatherKitService,
    private val weatherKitRemoteMapper: WeatherKitRemoteMapper,
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation> {
        val locale = Locale.getDefault()
        val countryCode = countryCode.uppercase().ifEmpty { "US" }
        return flow {
            emit(
                weatherKitService.getWeather(
                    locale = locale.language + "-" + locale.country,
                    latitude = latitude,
                    longitude = longitude,
                    countryCode = countryCode,
                    timezone = timezone,
                )
            )
        }.map { response ->
            try {
                weatherKitRemoteMapper.map(response, latitude, longitude, timezone, countryCode)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getWeather(name: String): Flow<WeatherLocation> {
        throw Exception("No name fecthing for weatherkit")
    }
}