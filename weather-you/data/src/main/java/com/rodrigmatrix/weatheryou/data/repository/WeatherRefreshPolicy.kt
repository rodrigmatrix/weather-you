package com.rodrigmatrix.weatheryou.data.repository

/** Limits a user-requested forced refresh to the location currently being viewed. */
internal object WeatherRefreshPolicy {
    fun shouldForceCurrentLocation(
        forceUpdate: Boolean,
        forceUpdateLocationIsCurrent: Boolean?,
    ): Boolean = forceUpdate && (forceUpdateLocationIsCurrent == null || forceUpdateLocationIsCurrent)

    fun shouldForceSavedLocation(
        forceUpdate: Boolean,
        forceUpdateLocationIsCurrent: Boolean?,
        forceUpdateLatitude: Double?,
        forceUpdateLongitude: Double?,
        latitude: Double,
        longitude: Double,
    ): Boolean = forceUpdate && (
        forceUpdateLocationIsCurrent == null ||
            (forceUpdateLocationIsCurrent == false &&
                forceUpdateLatitude == latitude &&
                forceUpdateLongitude == longitude)
        )
}
