package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Minute(
    @SerialName("precipitationChance")
    val precipitationChance: Double? = null,
    @SerialName("precipitationIntensity")
    val precipitationIntensity: Double? = null,
    @SerialName("startTime")
    val startTime: String? = null
)