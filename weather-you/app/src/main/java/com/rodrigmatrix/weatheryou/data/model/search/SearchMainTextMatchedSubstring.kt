package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMainTextMatchedSubstring(
    @SerialName("length")
    val length: Int? = null,
    @SerialName("offset")
    val offset: Int? = null
)