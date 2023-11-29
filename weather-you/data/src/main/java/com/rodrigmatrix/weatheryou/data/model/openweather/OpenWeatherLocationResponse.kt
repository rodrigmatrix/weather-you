package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherLocationResponse(
    @SerialName("current")
    val current: OpenWeatherCurrent? = null,
    @SerialName("daily")
    val daily: List<OpenWeatherDaily>? = null,
    @SerialName("hourly")
    val hourly: List<OpenWeatherHourly>? = null,
    @SerialName("lat")
    val lat: Double? = null,
    @SerialName("lon")
    val lon: Double? = null,
    @SerialName("minutely")
    val minutely: List<OpenWeatherMinutely>? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezone_offset")
    val timezoneOffset: Int? = null,
    @SerialName("name")
    val name: String? = null,
)