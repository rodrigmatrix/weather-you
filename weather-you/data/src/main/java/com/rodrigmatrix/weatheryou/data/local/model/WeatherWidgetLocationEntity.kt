package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherWidgetLocationCache")
data class WeatherWidgetLocationEntity(
    @PrimaryKey var id: Int = 0,
    var name: String,
    var latitude: Double,
    var longitude: Double,
    var currentWeather: Double,
    var currentCondition: String,
    var maxWeather: Double,
    var minWeather: Double,
    var lastUpdate: String,
    var nextHourWeather: Double,
    var nextHourCondition: String,
    var nextTwoHoursWeather: Double,
    var nextTwoHoursCondition: String,
    var nextThreeHoursWeather: Double,
    var nextThreeHoursCondition: String,
    var nextFourHoursWeather: Double,
    var nextFourHoursCondition: String,
    var tomorrowMaxWeather: Double,
    var tomorrowMinWeather: Double,
    var tomorrowCondition: String,
    var nextTwoDaysMaxWeather: Double,
    var nextTwoDaysMinWeather: Double,
    var nextTwoDaysCondition: String,
)
