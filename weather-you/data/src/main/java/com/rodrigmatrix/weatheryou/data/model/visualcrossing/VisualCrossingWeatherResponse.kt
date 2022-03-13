package com.rodrigmatrix.weatheryou.data.model.visualcrossing


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisualCrossingWeatherResponse(
    @SerialName("address")
    val address: String? = null,
    @SerialName("currentConditions")
    val currentConditions: CurrentConditionsResponse? = null,
    @SerialName("days")
    val days: List<DayResponse>? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("queryCost")
    val queryCost: Int? = null,
    @SerialName("resolvedAddress")
    val resolvedAddress: String? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("tzoffset")
    val tzoffset: Double? = null
)