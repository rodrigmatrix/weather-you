package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.data.model.search.NinjasCityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNinjasService {

    @GET("v1/city")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("limit") limit: Int = 8,
    ): ArrayList<NinjasCityResponse>
}