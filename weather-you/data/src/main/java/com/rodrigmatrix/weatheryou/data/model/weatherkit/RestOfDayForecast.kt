package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestOfDayForecast(
    @SerialName("cloudCover")
    val cloudCover: Double? = null,
    @SerialName("conditionCode")
    val conditionCode: String? = null,
    @SerialName("forecastEnd")
    val forecastEnd: String? = null,
    @SerialName("forecastStart")
    val forecastStart: String? = null,
    @SerialName("humidity")
    val humidity: Double? = null,
    @SerialName("precipitationAmount")
    val precipitationAmount: Double? = null,
    @SerialName("precipitationChance")
    val precipitationChance: Double? = null,
    @SerialName("precipitationType")
    val precipitationType: String? = null,
    @SerialName("snowfallAmount")
    val snowfallAmount: Double? = null,
    @SerialName("temperatureMax")
    val temperatureMax: Double? = null,
    @SerialName("temperatureMin")
    val temperatureMin: Double? = null,
    @SerialName("windDirection")
    val windDirection: Int? = null,
    @SerialName("windGustSpeedMax")
    val windGustSpeedMax: Double? = null,
    @SerialName("windSpeed")
    val windSpeed: Double? = null,
    @SerialName("windSpeedMax")
    val windSpeedMax: Double? = null
)