package com.rodrigmatrix.weatheryou.data.model.search

import kotlinx.serialization.Serializable

@Serializable
data class NinjasCityResponse(
    val country: String?,
    val is_capital: Boolean?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val population: Int?
)