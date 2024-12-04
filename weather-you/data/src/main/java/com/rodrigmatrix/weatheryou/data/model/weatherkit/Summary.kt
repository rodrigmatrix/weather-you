package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("condition")
    val condition: String? = null,
    @SerialName("precipitationChance")
    val precipitationChance: Double? = null,
    @SerialName("precipitationIntensity")
    val precipitationIntensity: Double? = null,
    @SerialName("startTime")
    val startTime: String? = null
)