package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPrediction(
    @SerialName("description")
    val description: String? = null,
    @SerialName("matched_substrings")
    val matchedSubstrings: List<SearchMatchedSubstring>? = null,
    @SerialName("place_id")
    val placeId: String? = null,
    @SerialName("reference")
    val reference: String? = null,
    @SerialName("structured_formatting")
    val structuredFormatting: SearchStructuredFormatting? = null,
    @SerialName("terms")
    val terms: List<SearchTerm>? = null,
    @SerialName("types")
    val types: List<String>? = null
)