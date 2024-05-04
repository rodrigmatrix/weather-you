package com.rodrigmatrix.weatheryou.domain.model

data class Location(
    val city: String,
    val state: String,
    val lat: Double,
    val long: Double,
    val country: String,
    val countryCode: String,
)