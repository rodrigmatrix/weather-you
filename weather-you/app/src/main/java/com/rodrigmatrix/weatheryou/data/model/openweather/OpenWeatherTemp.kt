package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherTemp(
    @SerialName("day")
    val day: Double? = null,
    @SerialName("eve")
    val eve: Double? = null,
    @SerialName("max")
    val max: Double? = null,
    @SerialName("min")
    val min: Double? = null,
    @SerialName("morn")
    val morn: Double? = null,
    @SerialName("night")
    val night: Double? = null
)