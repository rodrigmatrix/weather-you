package com.rodrigmatrix.weatheryou.data.local.database

import androidx.room.TypeConverter
import com.rodrigmatrix.weatheryou.data.local.model.WeatherDayEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherHourEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherDatabaseConverters {

    @TypeConverter
    fun fromWeatherDayList(value : List<WeatherDayEntity>) = Json.encodeToString(value)

    @TypeConverter
    fun toWeatherDayList(value: String) = Json.decodeFromString<List<WeatherDayEntity>>(value)

    @TypeConverter
    fun fromWeatherHourList(value : List<WeatherHourEntity>) = Json.encodeToString(value)

    @TypeConverter
    fun toWeatherHourList(value: String) = Json.decodeFromString<List<WeatherHourEntity>>(value)
}