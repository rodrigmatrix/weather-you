package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.data.model.weatherkit.WeatherKitLocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherKitService {

    @GET("v1/weather/{locale}/{latitude}/{longitude}")
    suspend fun getWeather(
        @Path("locale") locale: String,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Query("dataSets") dataSets: String = "currentWeather,forecastDaily,forecastHourly,forecastNextHour,weatherAlerts",
        @Query("timezone") timezone: String = "",
        @Query("countryCode") countryCode: String,
    ): WeatherKitLocationResponse
}