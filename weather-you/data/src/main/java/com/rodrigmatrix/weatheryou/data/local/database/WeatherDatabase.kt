package com.rodrigmatrix.weatheryou.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(WeatherDatabaseConverters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDAO

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_you_weather_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}