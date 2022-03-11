package com.rodrigmatrix.weatheryou.data.local

import com.rodrigmatrix.weatheryou.data.local.dao.WeatherDAO
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.mapper.CurrentLocationToEntityMapper
import kotlinx.coroutines.flow.*

private const val FIRST_INDEX = 0

class WeatherLocalDataSourceImpl(
    private val weatherDAO: WeatherDAO,
    private val userLocationDataSource: UserLocationDataSource,
    private val currentLocationToEntityMapper: CurrentLocationToEntityMapper
) : WeatherLocalDataSource {

    override fun getAllLocations(): Flow<List<WeatherLocationEntity>> {
        return weatherDAO.getAllLocations().map { locationsList ->
            val currentLocation = userLocationDataSource.getCurrentLocation().firstOrNull()
            val mutableLocationsList = locationsList.toMutableList()
            if (currentLocation != null) {
                mutableLocationsList.add(
                    FIRST_INDEX,
                    currentLocationToEntityMapper.map(currentLocation)
                )
            }
            return@map mutableLocationsList
        }
    }

    override fun addLocation(location: WeatherLocationEntity): Flow<Unit> {
        return flow {
            emit(weatherDAO.addLocation(location))
        }
    }

    override fun deleteLocation(latitude: Double, longitude: Double): Flow<Unit> {
        return flow {
            emit(weatherDAO.deleteLocation(latitude, longitude))
        }
    }
}