package com.rodrigmatrix.weatheryou.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rodrigmatrix.weatheryou.data.local.model.CurrentLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDAO {

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<WeatherLocationEntity>>

    @Query("SELECT * FROM currentLocation")
    fun getCurrentLocation(): Flow<CurrentLocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentLocation(currentLocation: CurrentLocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLocation(weatherLocation: WeatherLocationEntity)

    @Query("DELETE FROM locations WHERE id LIKE :id")
    fun deleteLocation(id: Int)
}