package com.rodrigmatrix.weatheryou.data.remote

import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VisualCrossingRemoteDataSourceImpl(
    private val visualCrossingService: VisualCrossingService
) : VisualCrossingRemoteDataSource {

    override fun getWeather(location: String, unit: String): Flow<VisualCrossingWeatherResponse> {
        return flow {
            emit(
                visualCrossingService.getWeather(
                    location = location,
                    unitGroup = unit
                )
            )
        }
    }
}