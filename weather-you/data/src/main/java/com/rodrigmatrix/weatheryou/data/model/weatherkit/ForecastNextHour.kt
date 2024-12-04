package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastNextHour(
    @SerialName("forecastEnd")
    val forecastEnd: String? = "",
    @SerialName("forecastStart")
    val forecastStart: String? = "",
    @SerialName("metadata")
    val metadata: MetadataXXX? = MetadataXXX(),
    @SerialName("minutes")
    val minutes: List<Minute>? = listOf(),
    @SerialName("name")
    val name: String? = "",
    @SerialName("summary")
    val summary: List<Summary>? = listOf()
)