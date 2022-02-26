package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

class WeatherLocationDomainToEntityMapper {

    fun map(source: WeatherLocation): WeatherLocationEntity {
        return WeatherLocationEntity(
            source.name
        )
    }
}