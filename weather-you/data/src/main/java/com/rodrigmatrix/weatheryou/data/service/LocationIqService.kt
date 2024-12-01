package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.data.model.locationiq.LocationIqSearchResponseItem
import com.rodrigmatrix.weatheryou.data.model.locationiq.TimezoneResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface LocationIqService {

    @GET("autocomplete")
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("tag") tag: String = "",
        @Query("accept-language") acceptLanguage: String = Locale.getDefault().language,
        @Query("limit") limit: Int = 5,
    ): List<LocationIqSearchResponseItem>

    @GET("timezone")
    suspend fun getTimezone(
        @Query("lat") lat: Double,
        @Query("lon") tag: Double,
    ): TimezoneResponse
}