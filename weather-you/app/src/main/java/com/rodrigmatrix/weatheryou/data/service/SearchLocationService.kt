package com.rodrigmatrix.weatheryou.data.service

import com.rodrigmatrix.weatheryou.data.model.search.SearchAutoCompleteResponse
import com.rodrigmatrix.weatheryou.data.model.search.SearchLocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchLocationService {

    @GET("place/autocomplete/json")
    suspend fun search(
        @Query("input") query: String,
        @Query("language") language: String,
        @Query("types") types: String = "(cities)"
    ): SearchAutoCompleteResponse

    @GET("geocode/json")
    suspend fun getLocationDetails(
        @Query("place_id") placeId: String,
        @Query("language") language: String
    ): SearchLocationResponse
}