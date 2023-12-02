package com.rodrigmatrix.weatheryou.domain.model

data class SavedLocation(
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val isCurrentLocation: Boolean,
    val isWidgetLocation: Boolean,
)