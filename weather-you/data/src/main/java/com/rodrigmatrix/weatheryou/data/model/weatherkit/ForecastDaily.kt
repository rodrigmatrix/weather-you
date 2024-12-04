package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDaily(
    @SerialName("days")
    val days: List<Day>? = listOf(),
    @SerialName("metadata")
    val metadata: Metadata? = Metadata(),
    @SerialName("name")
    val name: String? = ""
)