package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.model.VisualCrossingWeatherResponse
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

class VisualCrossingLocalMapper {

    fun map(source: WeatherLocation): WeatherLocationEntity {
        return WeatherLocationEntity(
            source.name
        )
    }
}