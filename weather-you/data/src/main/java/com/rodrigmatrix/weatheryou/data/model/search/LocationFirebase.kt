package com.rodrigmatrix.weatheryou.data.model.search

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class LocationFirebase(
    val name: String,
    val country_code: String,
    val cou_name_en: String,
    val coordinates: LocationFirebaseCoordinates,
)

@IgnoreExtraProperties
data class LocationFirebaseCoordinates(
    val lat: Double,
    val lon: Double,
)
