package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTerm(
    @SerialName("offset")
    val offset: Int? = null,
    @SerialName("value")
    val value: String? = null
)