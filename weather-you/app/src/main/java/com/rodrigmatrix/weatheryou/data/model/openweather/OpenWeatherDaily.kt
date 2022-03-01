package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherDaily(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val datetime: Long? = null,
    @SerialName("feels_like")
    val feelsLike: OpenWeatherFeelsLike? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("moon_phase")
    val moonPhase: Double? = null,
    @SerialName("moonrise")
    val moonrise: Int? = null,
    @SerialName("moonset")
    val moonset: Int? = null,
    @SerialName("pop")
    val pop: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("rain")
    val rain: Double? = null,
    @SerialName("sunrise")
    val sunrise: Long? = null,
    @SerialName("sunset")
    val sunset: Long? = null,
    @SerialName("temp")
    val temp: OpenWeatherTemp? = null,
    @SerialName("uvi")
    val uvi: Double? = null,
    @SerialName("weather")
    val weather: List<OpenWeatherWeather>? = null,
    @SerialName("wind_deg")
    val windDeg: Int? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)