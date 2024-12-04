package com.rodrigmatrix.weatheryou.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodrigmatrix.weatheryou.data.local.dao.WidgetDataDao
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity

@Database(
    entities = [
        WeatherWidgetLocationEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(WeatherDatabaseConverters::class)
abstract class WidgetDatabase : RoomDatabase() {

    abstract fun widgetDataDao(): WidgetDataDao

    companion object {
        @Volatile private var instance: WidgetDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WidgetDatabase::class.java,
            "weather_you_widget_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}