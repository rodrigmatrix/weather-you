package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastHourly(
    @SerialName("hours")
    val hours: List<Hour>? = listOf(),
    @SerialName("metadata")
    val metadata: Metadata? = Metadata(),
    @SerialName("name")
    val name: String? = ""
)