package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.LocationsDAO
import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.dao.WidgetDataDao
import com.rodrigmatrix.weatheryou.data.local.model.CurrentLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO,
    private val widgetDataDao: WidgetDataDao,
    private val locationsDAO: LocationsDAO,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return locationsDAO.getAllLocations()
            .flowOn(coroutineDispatcher)
    }

    override fun upsertLocation(location: WeatherLocationEntity): Flow<Unit> {
        return flow {
            emit(locationsDAO.upsertLocation(location))
        }.flowOn(coroutineDispatcher)
    }

    override fun getWidgetLocation(widgetId: String): Flow<WeatherWidgetLocationEntity?> {
        return widgetDataDao.getWidgetLocation(widgetId)
    }

    override fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        return flow {
            weatherDAO.deleteWeather(weatherLocation.latitude, weatherLocation.longitude)
            emit(locationsDAO.deleteLocation(weatherLocation.id))
        }.flowOn(coroutineDispatcher)
    }

    override fun getCurrentLocation(): Flow<CurrentLocationEntity?> {
        return locationsDAO.getCurrentLocation()
            .flowOn(coroutineDispatcher)
    }

    override fun upsertCurrentWeather(weatherEntity: WeatherEntity): Flow<Unit> {
        return flow {
            weatherDAO.deleteCurrentLocationWeather()
            emit(weatherDAO.upsertWeather(weatherEntity))
        }.flowOn(coroutineDispatcher)
    }

    override fun upsertCurrentLocation(currentLocation: CurrentLocationEntity): Flow<Unit> {
        return flow {
            emit(locationsDAO.upsertCurrentLocation(currentLocation))
        }.flowOn(coroutineDispatcher)
    }

    override fun getWeather(latitude: Double, longitude: Double): Flow<WeatherEntity?> {
        return weatherDAO.getLocationWeather(latitude, longitude)
            .flowOn(coroutineDispatcher)
    }

    override fun getCurrentLocationWeather(): Flow<WeatherEntity?> {
        return weatherDAO.getCurrentLocationWeather()
            .flowOn(coroutineDispatcher)
    }

    override fun upsertWeather(weatherEntity: WeatherEntity): Flow<Unit> {
        return flow {
            emit(weatherDAO.upsertWeather(weatherEntity))
        }.flowOn(coroutineDispatcher)
    }

    override fun getWidgetWeatherList(): Flow<List<WeatherWidgetLocationEntity>> {
        return widgetDataDao.getWidgetLocations()
            .flowOn(coroutineDispatcher)
    }

    override fun saveWidgetLocation(location: WeatherWidgetLocationEntity): Flow<Unit> {
        return flow {
            emit(widgetDataDao.setWidgetData(location))
        }.flowOn(coroutineDispatcher)
    }

    override fun deleteWidgetLocation(widgetId: String): Flow<Unit> {
        return flow {
            widgetDataDao.deleteWidgetData(widgetId)
            emit(Unit)
        }.flowOn(coroutineDispatcher)
    }
}