package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.LocalLocation
import com.rodrigmatrix.weatheryou.domain.model.Location

fun List<LocalLocation>.toLocationList() = this.map {
    Location(
        city = it.city,
        state = it.admin_name,
        lat = it.lat,
        long = it.lng,
        country = it.country,
        countryCode = it.iso2,
    )
}