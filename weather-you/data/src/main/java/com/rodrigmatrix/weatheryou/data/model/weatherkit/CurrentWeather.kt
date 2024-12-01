package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    @SerialName("asOf")
    val asOf: String? = null,
    @SerialName("cloudCover")
    val cloudCover: Double? = null,
    @SerialName("cloudCoverHighAltPct")
    val cloudCoverHighAltPct: Double? = null,
    @SerialName("cloudCoverLowAltPct")
    val cloudCoverLowAltPct: Double? = null,
    @SerialName("cloudCoverMidAltPct")
    val cloudCoverMidAltPct: Double? = null,
    @SerialName("conditionCode")
    val conditionCode: String? = null,
    @SerialName("daylight")
    val daylight: Boolean? = null,
    @SerialName("humidity")
    val humidity: Double? = null,
    @SerialName("metadata")
    val metadata: Metadata? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("precipitationIntensity")
    val precipitationIntensity: Double? = null,
    @SerialName("pressure")
    val pressure: Double? = null,
    @SerialName("pressureTrend")
    val pressureTrend: String? = null,
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