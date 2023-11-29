package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.visualcrossing.Cities
import com.rodrigmatrix.weatheryou.domain.model.City

class FamousCitiesMapper {

    fun map(source: List<Cities>): List<City> {
        return source.map { city ->
            City(
                name = city.displayName,
                lat = city.lat,
                long = city.long,
                image = city.image
            )
        }
    }
}