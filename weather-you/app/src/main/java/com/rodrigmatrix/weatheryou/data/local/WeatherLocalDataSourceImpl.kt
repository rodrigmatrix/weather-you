package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return weatherDAO.getAllLocations()
    }

    override fun addLocation(location: WeatherLocationEntity): Flow<Unit> {
        return flow {
            emit(weatherDAO.addLocation(location))
        }
    }

    override fun deleteLocation(locationName: String): Flow<Unit> {
        return flow {
            emit(weatherDAO.deleteLocation(locationName))
        }
    }
}