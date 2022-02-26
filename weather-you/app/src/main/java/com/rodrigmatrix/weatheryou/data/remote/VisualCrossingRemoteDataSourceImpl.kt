package com.rodrigmatrix.weatheryou.data.remote

import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VisualCrossingRemoteDataSourceImpl(
    private val visualCrossingService: VisualCrossingService,
    private val visualCrossingRemoteMapper: VisualCrossingRemoteMapper
) : WeatherYouRemoteDataSource {

    override fun getWeather(location: String, unit: String): Flow<WeatherLocation> {
        return flow {
            emit(
                visualCrossingService.getWeather(
                    location = location,
                    unitGroup = unit
                )
            )
        }.map(visualCrossingRemoteMapper::map)
    }
}