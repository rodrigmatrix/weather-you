package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.rodrigmatrix.weatheryou.domain.model.MoonPhase
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Entity(tableName = "weather", primaryKeys = ["latitude", "longitude"])
data class WeatherEntity(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean,
    val currentWeather: Double,
    val currentCondition: String,
    val maxTemperature: Double,
    val lowestTemperature: Double,
    val feelsLike: Double,
    val currentTime: String,
    val isDaylight: Boolean,
    val expirationDate: String,
    val timeZone: String,
    val precipitationProbability: Double,
    val precipitationType: String,
    val humidity: Double,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val uvIndex: Double,
    val sunrise: String,
    val sunset: String,
    val visibility: Double,
    val pressure: Double,
    val countryCode: String,
    val minWeekTemperature: Double,
    val maxWeekTemperature: Double,
    val cloudCover: Double,
    @ColumnInfo("days") val days: List<WeatherDayEntity>,
    @ColumnInfo("hours") val hours: List<WeatherHourEntity>,
)

@Serializable
data class WeatherDayEntity(
    val dateTime: String,
    val weatherCondition: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val hours: List<WeatherHourEntity>,
    val precipitationProbability: Double,
    val precipitationType: String,
    val windSpeed: Double,
    val humidity: Double,
    val sunrise: String,
    val sunset: String,
    val precipitationAmount: Double,
    val snowfallAmount: Double,
    val solarNoon: String,
    val solarMidnight: String,
    val moonPhase: String,
    val sunriseAstronomical: String,
    val sunsetAstronomical: String,
    val sunriseNautical: String,
    val sunsetNautical: String,
    val sunriseCivil: String,
    val sunsetCivil: String,
)

@Serializable
data class WeatherHourEntity(
    val temperature: Double,
    val dateTime: String,
    val isDaylight: Boolean = true,
    val weatherCondition: String,
    val precipitationProbability: Double,
    val precipitationType: String,
    val feelsLike: Double,
    val cloudCover: Double,
    val humidity: Double,
    val visibility: Double,
    val precipitationAmount: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val uvIndex: Double,
    val snowfallIntensity: Double,
)