package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.VisualCrossingRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl(
    private val visualCrossingRemoteDataSource: VisualCrossingRemoteDataSource,
    private val visualCrossingRemoteMapper: VisualCrossingRemoteMapper
) : WeatherRepository {

    override fun addLocation(locationName: String): Flow<WeatherLocation> {
        return visualCrossingRemoteDataSource.getWeather(
            locationName,
            VisualCrossingUnits.Metric.unit
        ).map(visualCrossingRemoteMapper::map)
    }

    override fun getLocation(resolvedAddress: String): Flow<WeatherLocation> {
        TODO("Not yet implemented")
    }

    override fun getLocationsList(): Flow<List<WeatherLocation>> {
        return flow {
            val list = listOf(
                "toronto",
                "new york",
                "Fortaleza ceara"
            ).map {
                addLocation(it).first()
            }
            emit(list)
        }
    }

    override fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit> {
        TODO("Not yet implemented")
    }
}