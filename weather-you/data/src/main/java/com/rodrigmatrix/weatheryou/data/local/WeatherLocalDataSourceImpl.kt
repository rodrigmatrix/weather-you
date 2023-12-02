package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.dao.WidgetDataDao
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO,
    private val widgetDataDao: WidgetDataDao,
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return weatherDAO.getAllLocations()
    }

    override fun addLocation(location: WeatherLocationEntity): Flow<Unit> {
        return flow {
            emit(weatherDAO.addLocation(location))
        }
    }

    override fun deleteLocation(id: Int): Flow<Unit> {
        return flow {
            emit(weatherDAO.deleteLocation(id))
        }
    }

    override fun getWidgetLocation(): Flow<WeatherWidgetLocationEntity?> {
        return widgetDataDao.getWidgetLocation()
    }

    override fun getSavedLocation(): Flow<WeatherLocationEntity?> {
        return weatherDAO.getSavedWidgetLocation()
    }
}