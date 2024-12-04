package com.rodrigmatrix.weatheryou.data.model.locationiq


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimezoneResponse(
    @SerialName("timezone")
    val timezone: Timezone? = null
)

@Serializable
data class Timezone(
    @SerialName("name")
    val name: String? = null,
    @SerialName("now_in_dst")
    val nowInDst: Int? = null,
    @SerialName("offset_sec")
    val offsetSec: Int? = null,
    @SerialName("short_name")
    val shortName: String? = null
)