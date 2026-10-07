package com.rodrigmatrix.weatheryou.data.repository

/** Limits a user-requested forced refresh to the location currently being viewed. */
internal object WeatherRefreshPolicy {
    private const val EARTH_RADIUS_METERS = 6_371_000.0
    private const val CURRENT_LOCATION_REFRESH_DISTANCE_METERS = 5_000.0

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

    fun shouldRefreshCurrentLocationWeather(
        forceUpdate: Boolean,
        weatherExpired: Boolean,
        cachedLatitude: Double?,
        cachedLongitude: Double?,
        currentLatitude: Double,
        currentLongitude: Double,
    ): Boolean {
        if (forceUpdate || weatherExpired || cachedLatitude == null || cachedLongitude == null) return true
        return distanceMeters(cachedLatitude, cachedLongitude, currentLatitude, currentLongitude) >=
            CURRENT_LOCATION_REFRESH_DISTANCE_METERS
    }

    private fun distanceMeters(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
    ): Double {
        if (listOf(startLatitude, startLongitude, endLatitude, endLongitude).any { !it.isFinite() }) {
            return Double.POSITIVE_INFINITY
        }
        val startLatitudeRadians = Math.toRadians(startLatitude)
        val endLatitudeRadians = Math.toRadians(endLatitude)
        val latitudeDeltaRadians = Math.toRadians(endLatitude - startLatitude)
        val longitudeDeltaRadians = Math.toRadians(endLongitude - startLongitude)
        val haversine = kotlin.math.sin(latitudeDeltaRadians / 2).let { it * it } +
            kotlin.math.cos(startLatitudeRadians) * kotlin.math.cos(endLatitudeRadians) *
            kotlin.math.sin(longitudeDeltaRadians / 2).let { it * it }
        return 2 * EARTH_RADIUS_METERS * kotlin.math.asin(kotlin.math.sqrt(haversine.coerceIn(0.0, 1.0)))
    }
}
