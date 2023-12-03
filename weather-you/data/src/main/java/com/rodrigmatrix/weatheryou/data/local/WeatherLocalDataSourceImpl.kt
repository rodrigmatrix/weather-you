package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.dao.WidgetDataDao
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO,
    private val widgetDataDao: WidgetDataDao,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return weatherDAO.getAllLocations()
            .flowOn(coroutineDispatcher)
    }

    override fun addLocation(location: WeatherLocationEntity): Flow<Unit> {
        return flow {
            weatherDAO.addLocation(location)
            emit(Unit)
        }.flowOn(coroutineDispatcher)
    }

    override fun deleteLocation(id: Int): Flow<Unit> {
        return flow {
            emit(weatherDAO.deleteLocation(id))
        }.flowOn(coroutineDispatcher)
    }

    override fun getWidgetLocation(): Flow<WeatherWidgetLocationEntity?> {
        return widgetDataDao.getWidgetLocation()
            .flowOn(coroutineDispatcher)
    }

    override fun getWidgetWeather(): Flow<WeatherWidgetLocationEntity?> {
        return widgetDataDao.getWidgetLocation()
            .flowOn(coroutineDispatcher)
    }

    override fun saveWidgetLocation(location: WeatherWidgetLocationEntity): Flow<Unit> {
        return flow {
            widgetDataDao.setWidgetData(location)
            emit(Unit)
        }.flowOn(coroutineDispatcher)
    }

    override fun deleteWidgetLocation(): Flow<Unit> {
        return flow {
            widgetDataDao.deleteWidgetData()
            emit(Unit)
        }.flowOn(coroutineDispatcher)
    }
}