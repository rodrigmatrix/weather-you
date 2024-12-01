package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    @SerialName("cloudCover")
    val cloudCover: Double? = null,
    @SerialName("conditionCode")
    val conditionCode: String? = null,
    @SerialName("daylight")
    val daylight: Boolean? = null,
    @SerialName("forecastStart")
    val forecastStart: String? = null,
    @SerialName("humidity")
    val humidity: Double? = null,
    @SerialName("precipitationAmount")
    val precipitationAmount: Double? = null,
    @SerialName("precipitationChance")
    val precipitationChance: Double? = null,
    @SerialName("precipitationIntensity")
    val precipitationIntensity: Double? = null,
    @SerialName("precipitationType")
    val precipitationType: String? = null,
    @SerialName("pressure")
    val pressure: Double? = null,
    @SerialName("pressureTrend")
    val pressureTrend: String? = null,
    @SerialName("snowfallAmount")
    val snowfallAmount: Double? = null,
    @SerialName("snowfallIntensity")
    val snowfallIntensity: Double? = null,
    @SerialName("temperature")
    val temperature: Double? = null,
    @SerialName("temperatureApparent")
    val temperatureApparent: Double? = null,
    @SerialName("temperatureDewPoint")
    val temperatureDewPoint: Double? = null,
    @SerialName("uvIndex")
    val uvIndex: Int? = null,
    @SerialName("visibility")
    val visibility: Double? = null,
    @SerialName("windDirection")
    val windDirection: Int? = null,
    @SerialName("windGust")
    val windGust: Double? = null,
    @SerialName("windSpeed")
    val windSpeed: Double? = null
)