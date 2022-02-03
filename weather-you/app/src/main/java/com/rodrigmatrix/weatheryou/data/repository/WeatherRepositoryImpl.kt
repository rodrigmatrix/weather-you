package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingLocalMapper
import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.VisualCrossingRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.*

class WeatherRepositoryImpl(
    private val visualCrossingRemoteDataSource: VisualCrossingRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val visualCrossingRemoteMapper: VisualCrossingRemoteMapper,
    private val visualCrossingLocalMapper: VisualCrossingLocalMapper
) : WeatherRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addLocation(locationName: String): Flow<Unit> {
        return visualCrossingRemoteDataSource.getWeather(
                locationName,
                getMetricUnit()
            ).flatMapLatest { locationResponse ->
                weatherLocalDataSource.addLocation(visualCrossingLocalMapper.map(locationResponse))
            }
    }

    override fun fetchLocation(resolvedAddress: String): Flow<WeatherLocation> {
        return visualCrossingRemoteDataSource.getWeather(
            resolvedAddress,
            getMetricUnit()
        ).map(visualCrossingRemoteMapper::map)
    }

    override fun fetchLocationsList(): Flow<List<WeatherLocation>> {
        return weatherLocalDataSource
            .getAllLocations()
            .map { weatherLocations ->
                weatherLocations.map {
                    fetchLocation(it.name).first()
                }
            }
    }

    override fun deleteLocation(locationName: String): Flow<Unit> {
        return weatherLocalDataSource.deleteLocation(locationName)
    }

    private fun getMetricUnit(): String {
        return when (Locale.getDefault().language) {
            Locale.UK.language -> VisualCrossingUnits.UK.unit
            Locale.US.language -> VisualCrossingUnits.US.unit
            else -> VisualCrossingUnits.Metric.unit
        }
    }
}