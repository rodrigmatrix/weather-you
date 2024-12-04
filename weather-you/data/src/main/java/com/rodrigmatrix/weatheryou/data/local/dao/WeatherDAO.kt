package com.rodrigmatrix.weatheryou.data.local.dao

import androidx.room.*
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE latitude LIKE :latitude AND longitude LIKE :longitude")
    fun getLocationWeather(latitude: Double, longitude: Double): Flow<WeatherEntity>

    @Query("SELECT * FROM weather WHERE isCurrentLocation LIKE 1")
    fun getCurrentLocationWeather(): Flow<WeatherEntity>

    @Query("DELETE FROM weather WHERE latitude LIKE :latitude AND longitude LIKE :longitude")
    fun deleteWeather(latitude: Double, longitude: Double)
    
    @Query("DELETE FROM weather WHERE isCurrentLocation LIKE 1")
    suspend fun deleteCurrentLocationWeather()
}