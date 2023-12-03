package com.rodrigmatrix.weatheryou.data.local.dao

import androidx.room.*
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<WeatherLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(weatherLocation: WeatherLocationEntity)

    @Query("DELETE FROM locations WHERE id LIKE :id")
    fun deleteLocation(id: Int)
}