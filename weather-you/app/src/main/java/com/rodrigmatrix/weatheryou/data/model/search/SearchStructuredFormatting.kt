package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchStructuredFormatting(
    @SerialName("main_text")
    val mainText: String? = null,
    @SerialName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<SearchMainTextMatchedSubstring>? = null,
    @SerialName("secondary_text")
    val secondaryText: String? = null
)