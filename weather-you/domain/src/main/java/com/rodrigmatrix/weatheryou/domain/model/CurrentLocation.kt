package com.rodrigmatrix.weatheryou.domain.model

import org.joda.time.DateTime

data class CurrentLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val countryCode: String,
    val timezone: String,
    val lastUpdate: DateTime,
)
