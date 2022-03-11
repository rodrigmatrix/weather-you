package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "locations", primaryKeys = ["latitude","longitude"])
data class WeatherLocationEntity(
    val latitude: Double,
    val longitude: Double,
    val name: String
)