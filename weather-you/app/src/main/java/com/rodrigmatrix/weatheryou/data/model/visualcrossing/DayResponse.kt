package com.rodrigmatrix.weatheryou.data.model.visualcrossing


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayResponse(
    @SerialName("cloudcover")
    val cloudcover: Double? = null,
    @SerialName("conditions")
    val conditions: String? = null,
    @SerialName("datetime")
    val datetime: String? = null,
    @SerialName("datetimeEpoch")
    val datetimeEpoch: Int? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("dew")
    val dew: Double? = null,
    @SerialName("feelslike")
    val feelslike: Double? = null,
    @SerialName("feelslikemax")
    val feelslikemax: Double? = null,
    @SerialName("feelslikemin")
    val feelslikemin: Double? = null,
    @SerialName("hours")
    val hours: List<HourResponse>? = null,
    @SerialName("humidity")
    val humidity: Double? = null,
    @SerialName("icon")
    val icon: String? = null,
    @SerialName("moonphase")
    val moonphase: Double? = null,
    @SerialName("precip")
    val precip: Double? = null,
    @SerialName("precipprob")
    val precipprob: Double? = null,
    @SerialName("preciptype")
    val preciptype: List<String>? = null,
    @SerialName("pressure")
    val pressure: Double? = null,
    @SerialName("severerisk")
    val severerisk: Double? = null,
    @SerialName("snow")
    val snow: Double? = null,
    @SerialName("snowdepth")
    val snowdepth: Double? = null,
    @SerialName("solarenergy")
    val solarenergy: Double? = null,
    @SerialName("solarradiation")
    val solarradiation: Double? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("stations")
    val stations: List<String>? = null,
    @SerialName("sunrise")
    val sunrise: String? = null,
    @SerialName("sunriseEpoch")
    val sunriseEpoch: Int? = null,
    @SerialName("sunset")
    val sunset: String? = null,
    @SerialName("sunsetEpoch")
    val sunsetEpoch: Int? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("tempmax")
    val tempmax: Double? = null,
    @SerialName("tempmin")
    val tempmin: Double? = null,
    @SerialName("uvindex")
    val uvindex: Double? = null,
    @SerialName("visibility")
    val visibility: Double? = null,
    @SerialName("winddir")
    val winddir: Double? = null,
    @SerialName("windgust")
    val windgust: Double? = null,
    @SerialName("windspeed")
    val windspeed: Double? = null
)