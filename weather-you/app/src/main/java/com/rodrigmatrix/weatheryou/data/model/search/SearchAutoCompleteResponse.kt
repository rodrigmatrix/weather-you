package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchAutoCompleteResponse(
    @SerialName("predictions")
    val predictions: List<SearchPrediction>? = null,
    @SerialName("status")
    val status: String? = null
)