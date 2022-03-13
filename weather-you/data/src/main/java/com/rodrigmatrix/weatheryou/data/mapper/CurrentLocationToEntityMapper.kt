package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.core.map.Mapper
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation

class CurrentLocationToEntityMapper: Mapper<CurrentLocation, WeatherLocationEntity>() {

    override fun map(source: CurrentLocation): WeatherLocationEntity {
        return WeatherLocationEntity(
            name = source.name,
            latitude = source.latitude,
            longitude = source.longitude
        )
    }
}
