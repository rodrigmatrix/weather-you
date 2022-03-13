package com.rodrigmatrix.weatheryou.data.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("address_components")
    val addressComponents: List<SearchAddressComponent>? = null,
    @SerialName("formatted_address")
    val formattedAddress: String? = null,
    @SerialName("geometry")
    val geometry: SearchGeometry? = null,
    @SerialName("place_id")
    val placeId: String? = null,
    @SerialName("types")
    val types: List<String>? = null
)