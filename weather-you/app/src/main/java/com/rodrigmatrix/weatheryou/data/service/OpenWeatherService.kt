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
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") unit: String,
        @Query("lang") language: String,
        @Query("appid") key: String = BuildConfig.OPEN_WEATHER_TOKEN
    ): OpenWeatherLocationResponse
}