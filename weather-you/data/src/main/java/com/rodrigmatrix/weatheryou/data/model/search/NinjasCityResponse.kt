package com.rodrigmatrix.weatheryou.data.model.search

import kotlinx.serialization.Serializable

@Serializable
data class NinjasCityResponse(
    val country: String? = null,
    val is_capital: Boolean? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val population: Int? = null,
)