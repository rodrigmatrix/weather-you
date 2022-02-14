package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val FIRST_INDEX = 0

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO,
    private val userLocationDataSource: UserLocationDataSource
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return weatherDAO.getAllLocations().map { locationsList ->
            val currentLocation = userLocationDataSource.getCurrentLocation().first()
            val mutableLocationsList = locationsList.toMutableList()
            if (currentLocation.isNotEmpty()) {
                mutableLocationsList.add(FIRST_INDEX, WeatherLocationEntity(currentLocation))
            }
            mutableLocationsList
        }
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