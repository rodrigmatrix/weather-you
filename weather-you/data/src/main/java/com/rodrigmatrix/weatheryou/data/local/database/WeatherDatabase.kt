package com.rodrigmatrix.weatheryou.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.dao.WidgetDataDao
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity

@Database(
    entities = [WeatherLocationEntity::class, WeatherWidgetLocationEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationsDao(): WeatherDAO

    abstract fun widgetDataDao(): WidgetDataDao

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_you.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}