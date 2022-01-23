package com.rodrigmatrix.weatheryou.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisualCrossingWeatherResponse(
    @SerialName("address")
    val address: String?,
    @SerialName("currentConditions")
    val currentConditions: CurrentConditionsResponse?,
    @SerialName("days")
    val dayResponses: List<DayResponse>?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
    @SerialName("queryCost")
    val queryCost: Int?,
    @SerialName("resolvedAddress")
    val resolvedAddress: String?,
    @SerialName("stations")
    val stations: StationsResponse?,
    @SerialName("timezone")
    val timezone: String?,
    @SerialName("tzoffset")
    val tzoffset: Double?
)