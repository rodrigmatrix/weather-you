package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherMinutely(
    @SerialName("dt")
    val datetime: Long? = null,
    @SerialName("precipitation")
    val precipitation: Double? = null
)