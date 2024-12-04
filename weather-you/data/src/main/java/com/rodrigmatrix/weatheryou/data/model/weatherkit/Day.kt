package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Day(
    @SerialName("conditionCode")
    val conditionCode: String? = null,
    @SerialName("daytimeForecast")
    val daytimeForecast: DaytimeForecast? = null,
    @SerialName("forecastEnd")
    val forecastEnd: String? = null,
    @SerialName("forecastStart")
    val forecastStart: String? = null,
    @SerialName("maxUvIndex")
    val maxUvIndex: Int? = null,
    @SerialName("moonPhase")
    val moonPhase: String? = null,
    @SerialName("moonrise")
    val moonrise: String? = null,
    @SerialName("moonset")
    val moonset: String? = null,
    @SerialName("overnightForecast")
    val overnightForecast: OvernightForecast? = null,
    @SerialName("precipitationAmount")
    val precipitationAmount: Double? = null,
    @SerialName("precipitationChance")
    val precipitationChance: Double? = null,
    @SerialName("precipitationType")
    val precipitationType: String? = null,
    @SerialName("restOfDayForecast")
    val restOfDayForecast: RestOfDayForecast? = null,
    @SerialName("snowfallAmount")
    val snowfallAmount: Double? = null,
    @SerialName("solarMidnight")
    val solarMidnight: String? = null,
    @SerialName("solarNoon")
    val solarNoon: String? = null,
    @SerialName("sunrise")
    val sunrise: String? = null,
    @SerialName("sunriseAstronomical")
    val sunriseAstronomical: String? = null,
    @SerialName("sunriseCivil")
    val sunriseCivil: String? = null,
    @SerialName("sunriseNautical")
    val sunriseNautical: String? = null,
    @SerialName("sunset")
    val sunset: String? = null,
    @SerialName("sunsetAstronomical")
    val sunsetAstronomical: String? = null,
    @SerialName("sunsetCivil")
    val sunsetCivil: String? = null,
    @SerialName("sunsetNautical")
    val sunsetNautical: String? = null,
    @SerialName("temperatureMax")
    val temperatureMax: Double? = null,
    @SerialName("temperatureMin")
    val temperatureMin: Double? = null,
    @SerialName("windGustSpeedMax")
    val windGustSpeedMax: Double? = null,
    @SerialName("windSpeedAvg")
    val windSpeedAvg: Double? = null,
    @SerialName("windSpeedMax")
    val windSpeedMax: Double? = null
)