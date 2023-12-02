package com.rodrigmatrix.weatheryou.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDataDao {

    @Query("SELECT * FROM weatherWidgetLocationCache WHERE id LIKE 0")
    fun getWidgetLocation(): Flow<WeatherWidgetLocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setWidgetData(weatherWidgetLocationEntity: WeatherWidgetLocationEntity)
}