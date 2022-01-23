package com.rodrigmatrix.weatheryou.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationsResponse(
    @SerialName("AV699")
    val aV699: StationResponse?
)