package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.BuildConfig
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherLocationResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("timeline/{location}")
    suspend fun getWeather(
        @Path("q") location: String,
        @Query("units") unit: String = "metric",
        @Query("appid") key: String = BuildConfig.OPEN_WEATHER_TOKEN,
        @Query("lang") language: String = "en_US",
    ): OpenWeatherLocationResponse
}