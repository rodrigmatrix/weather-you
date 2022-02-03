package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse

class VisualCrossingLocalMapper {

    fun map(source: VisualCrossingWeatherResponse): WeatherLocationEntity {
        return WeatherLocationEntity(
            source.resolvedAddress.getLocationName()
        )
    }

    private fun String?.getLocationName(): String {
        return this.orEmpty().split(",").dropLast(1).joinToString()
    }
}