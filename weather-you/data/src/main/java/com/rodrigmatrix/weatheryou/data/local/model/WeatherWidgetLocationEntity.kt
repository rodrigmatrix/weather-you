package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherWidgetLocationCache")
data class WeatherWidgetLocationEntity(
    @PrimaryKey var id: String = "0",
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean,
    val name: String,
    val countryCode: String,
    val timeZone: String,
)
