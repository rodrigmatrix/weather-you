package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.mapper.WeatherLocationDomainToEntityMapper
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

class WeatherRepositoryImpl(
    private val weatherYouRemoteDataSource: WeatherYouRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherLocationDomainToEntityMapper: WeatherLocationDomainToEntityMapper
) : WeatherRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addLocation(locationName: String): Flow<Unit> {
        return weatherYouRemoteDataSource.getWeather(
                locationName,
                getMetricUnit()
            ).flatMapLatest { location ->
                weatherLocalDataSource.addLocation(weatherLocationDomainToEntityMapper.map(location))
            }
    }

    override fun fetchLocation(resolvedAddress: String): Flow<WeatherLocation> {
        return weatherYouRemoteDataSource.getWeather(
            resolvedAddress,
            getMetricUnit()
        )
    }

    override fun fetchLocationsList(): Flow<List<WeatherLocation>> {
        return weatherLocalDataSource
            .getAllLocations()
            .map { weatherLocations ->
                weatherLocations.mapNotNull {
                    fetchLocation(it.name)
                        .catch { emitAll(flowOf()) }
                        .firstOrNull()
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