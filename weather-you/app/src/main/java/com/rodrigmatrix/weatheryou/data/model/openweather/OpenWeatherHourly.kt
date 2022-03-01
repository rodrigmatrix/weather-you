package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherHourly(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val datetime: Long? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("pop")
    val pop: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("uvi")
    val uvi: Int? = null,
    @SerialName("visibility")
    val visibility: Int? = null,
    @SerialName("weather")
    val weather: List<OpenWeatherWeather>? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Int? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)