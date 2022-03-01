package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherFeelsLike(
    @SerialName("day")
    val day: Double? = null,
    @SerialName("eve")
    val eve: Double? = null,
    @SerialName("morn")
    val morn: Double? = null,
    @SerialName("night")
    val night: Double? = null
)