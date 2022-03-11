package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchGeometry(
    @SerialName("bounds")
    val bounds: SearchBounds? = null,
    @SerialName("location")
    val location: SearchLocation? = null,
    @SerialName("location_type")
    val locationType: String? = null,
    @SerialName("viewport")
    val viewport: SearchViewport? = null
)