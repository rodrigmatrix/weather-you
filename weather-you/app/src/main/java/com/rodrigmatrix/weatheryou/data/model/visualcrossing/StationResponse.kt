package com.rodrigmatrix.weatheryou.data.model.visualcrossing


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationResponse(
    @SerialName("contribution")
    val contribution: Double?,
    @SerialName("distance")
    val distance: Double?,
    @SerialName("id")
    val id: String?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
    @SerialName("name")
    val name: String?,
    @SerialName("quality")
    val quality: Int?,
    @SerialName("useCount")
    val useCount: Int?
)