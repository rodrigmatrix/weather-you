package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.SavedLocation

class WeatherLocationEntityToSavedLocationMapper {

    fun map(source: WeatherLocationEntity?): SavedLocation? =
        source?.let {
            SavedLocation(
                latitude = source.latitude,
                longitude = source.longitude,
                name = source.name,
                isCurrentLocation = false,
                isWidgetLocation = false,
            )
        }
}