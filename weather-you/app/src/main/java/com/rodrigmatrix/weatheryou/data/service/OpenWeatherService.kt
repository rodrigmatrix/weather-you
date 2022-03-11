package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.BuildConfig
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherLocationResponse
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") latitude: String = "33.44",
        @Query("lon") longitude: String = "-94.04",
        @Query("units") unit: String = "metric",
        @Query("appid") key: String = BuildConfig.OPEN_WEATHER_TOKEN,
        @Query("lang") language: String = "en_US",
    ): OpenWeatherLocationResponse
}