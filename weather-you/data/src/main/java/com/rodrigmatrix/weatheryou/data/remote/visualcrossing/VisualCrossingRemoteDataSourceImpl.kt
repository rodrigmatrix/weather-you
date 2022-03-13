package com.rodrigmatrix.weatheryou.data.remote.visualcrossing

import com.rodrigmatrix.weatheryou.data.mapper.VisualCrossingRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.visualcrossing.VisualCrossingUnits
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.data.service.VisualCrossingService
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

class VisualCrossingRemoteDataSourceImpl(
    private val visualCrossingService: VisualCrossingService,
    private val visualCrossingRemoteMapper: VisualCrossingRemoteMapper
) : WeatherYouRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double
    ): Flow<WeatherLocation> {
        return flow {
            emit(
                visualCrossingService.getWeatherWithCoordinates(
                    coordinates = "$latitude,$longitude",
                    unitGroup = getUnit()
                )
            )
        }.map(visualCrossingRemoteMapper::map)
    }

    private fun getUnit(): String {
        return when (Locale.getDefault().language) {
            Locale.UK.language -> VisualCrossingUnits.UK.unit
            Locale.US.language -> VisualCrossingUnits.US.unit
            else -> VisualCrossingUnits.Metric.unit
        }
    }
}