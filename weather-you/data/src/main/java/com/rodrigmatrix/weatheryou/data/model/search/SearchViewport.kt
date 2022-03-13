package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchViewport(
    @SerialName("northeast")
    val northeast: SearchNortheastX? = null,
    @SerialName("southwest")
    val southwest: SearchSouthwestX? = null
)