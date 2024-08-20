package com.rodrigmatrix.weatheryou.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalLocation(
    val city: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val iso2: String,
    val admin_name: String,
)