package com.rodrigmatrix.weatheryou.domain.model

data class SearchAutocompleteLocation(
    val name: String,
    val lat: Double,
    val long: Double,
    val countryCode: String,
    val timezone: String,
)