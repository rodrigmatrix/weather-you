package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchBounds(
    @SerialName("northeast")
    val northeast: SearchNortheast? = null,
    @SerialName("southwest")
    val southwest: SearchSouthwest? = null
)