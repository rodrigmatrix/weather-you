package com.rodrigmatrix.weatheryou.data.model.weatherkit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    @SerialName("attributionURL")
    val attributionURL: String? = null,
    @SerialName("expireTime")
    val expireTime: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("readTime")
    val readTime: String? = null,
    @SerialName("reportedTime")
    val reportedTime: String? = null,
    @SerialName("sourceType")
    val sourceType: String? = null,
    @SerialName("units")
    val units: String? = null,
    @SerialName("version")
    val version: Int? = null
)