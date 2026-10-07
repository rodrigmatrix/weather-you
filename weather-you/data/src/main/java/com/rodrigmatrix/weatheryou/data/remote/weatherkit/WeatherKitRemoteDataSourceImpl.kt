package com.rodrigmatrix.weatheryou.data.remote.weatherkit

import com.rodrigmatrix.weatheryou.data.mapper.WeatherKitRemoteMapper
import com.rodrigmatrix.weatheryou.data.remote.SingleFlight
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.WeatherKitService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class WeatherKitRemoteDataSourceImpl(
    private val weatherKitService: WeatherKitService,
    private val weatherKitRemoteMapper: WeatherKitRemoteMapper,
) : WeatherYouRemoteDataSource {
    private val inFlightRequests = SingleFlight<WeatherRequestKey, WeatherLocation>()

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        countryCode: String,
        timezone: String,
    ): Flow<WeatherLocation> {
        val locale = Locale.getDefault()
        val countryCode = countryCode.uppercase().ifEmpty { "US" }
        val requestLocale = locale.toLanguageTag()
        val requestKey = WeatherRequestKey(
            locale = requestLocale,
            latitude = latitude,
            longitude = longitude,
            countryCode = countryCode,
            timezone = timezone,
        )
        return flow {
            emit(inFlightRequests.execute(requestKey) {
                val response = weatherKitService.getWeather(
                    locale = requestLocale,
                    latitude = latitude,
                    longitude = longitude,
                    countryCode = countryCode,
                    timezone = timezone,
                )
                weatherKitRemoteMapper.map(response, latitude, longitude, timezone, countryCode)
            })
        }
    }

    override fun getWeather(name: String): Flow<WeatherLocation> {
        throw Exception("No name fecthing for weatherkit")
    }

    private data class WeatherRequestKey(
        val locale: String,
        val latitude: Double,
        val longitude: Double,
        val countryCode: String,
        val timezone: String,
    )
}
