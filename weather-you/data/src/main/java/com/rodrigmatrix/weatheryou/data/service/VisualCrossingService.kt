package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VisualCrossingService {

    @GET("timeline/{location}")
    suspend fun getWeather(
        @Path("location") location: String,
        @Query("unitGroup") unitGroup: String,
        @Query("include") days: String = "days,hours,current"
    ): VisualCrossingWeatherResponse

    @GET("timeline/{coordinates}")
    suspend fun getWeatherWithCoordinates(
        @Path("coordinates") coordinates: String,
        @Query("unitGroup") unitGroup: String,
        @Query("include") days: String = "days,hours,current"
    ): VisualCrossingWeatherResponse
}