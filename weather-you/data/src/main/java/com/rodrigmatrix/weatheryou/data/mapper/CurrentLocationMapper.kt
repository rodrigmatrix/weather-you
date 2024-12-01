package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.local.model.CurrentLocationEntity
import com.rodrigmatrix.weatheryou.domain.model.CurrentLocation

fun CurrentLocationEntity.toDomain() = CurrentLocation(
    name = name,
    latitude = latitude,
    longitude = longitude,
    countryCode = countryCode,
    timezone = timeZone,
    lastUpdate = lastUpdate.toDateTime(),
)

fun CurrentLocation.toEntity() = CurrentLocationEntity(
    name = name,
    latitude = latitude,
    longitude = longitude,
    countryCode = countryCode,
    timeZone = timezone,
    lastUpdate = lastUpdate.toEntityString(),
)