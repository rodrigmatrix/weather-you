package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.BuildConfig
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VisualCrossingService {

    @GET("timeline/{location}")
    suspend fun getWeather(
        @Path("location") location: String,
        @Query("unitGroup") unitGroup: String,
        @Query("include") days: String = "days,hours,current",
        @Query("key") key: String = BuildConfig.VISUAL_CODING_TOKEN,
    ): VisualCrossingWeatherResponse
}