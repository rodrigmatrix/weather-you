package com.rodrigmatrix.weatheryou.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodrigmatrix.weatheryou.data.local.dao.LocationsDAO
import com.rodrigmatrix.weatheryou.data.local.model.CurrentLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity

@Database(
    entities = [
        WeatherLocationEntity::class,
        CurrentLocationEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(WeatherDatabaseConverters::class)
abstract class LocationsDatabase : RoomDatabase() {

    abstract fun locationsDao(): LocationsDAO

    companion object {
        @Volatile private var instance: LocationsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LocationsDatabase::class.java,
            "weather_you_locations_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}