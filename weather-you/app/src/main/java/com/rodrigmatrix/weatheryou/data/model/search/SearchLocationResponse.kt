package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchLocationResponse(
    @SerialName("results")
    val results: List<SearchResult>? = null,
    @SerialName("status")
    val status: String? = null
)