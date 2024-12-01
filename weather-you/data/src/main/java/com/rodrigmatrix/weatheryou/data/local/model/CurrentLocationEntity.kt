package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currentLocation")
data class CurrentLocationEntity(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val countryCode: String,
    val timeZone: String,
    val lastUpdate: String,
) {
    @PrimaryKey
    var id: Int = 0
}