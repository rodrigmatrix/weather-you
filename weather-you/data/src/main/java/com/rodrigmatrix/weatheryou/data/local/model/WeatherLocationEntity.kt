package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class WeatherLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val orderIndex: Int,
    val longitude: Double,
    val name: String,
    val countryCode: String,
    val timeZone: String,
)