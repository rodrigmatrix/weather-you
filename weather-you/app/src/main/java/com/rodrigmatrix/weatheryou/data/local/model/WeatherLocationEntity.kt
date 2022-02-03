package com.rodrigmatrix.weatheryou.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "locations")
data class WeatherLocationEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String
)