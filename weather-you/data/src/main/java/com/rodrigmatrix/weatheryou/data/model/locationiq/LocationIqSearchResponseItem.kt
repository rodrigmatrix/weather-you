package com.rodrigmatrix.weatheryou.data.model.locationiq


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationIqSearchResponseItem(
    @SerialName("address")
    val address: Address? = null,
    @SerialName("boundingbox")
    val boundingbox: List<String?>? = null,
    @SerialName("class")
    val classX: String? = null,
    @SerialName("display_address")
    val displayAddress: String? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("display_place")
    val displayPlace: String? = null,
    @SerialName("lat")
    val lat: String? = null,
    @SerialName("licence")
    val licence: String? = null,
    @SerialName("lon")
    val lon: String? = null,
    @SerialName("osm_id")
    val osmId: String? = null,
    @SerialName("osm_type")
    val osmType: String? = null,
    @SerialName("place_id")
    val placeId: String? = null,
    @SerialName("type")
    val type: String? = null
)