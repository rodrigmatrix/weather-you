package com.rodrigmatrix.weatheryou.data.local

internal object LocationFreshnessPolicy {
    private const val NANOS_PER_MILLI = 1_000_000L
    const val MAX_CACHED_LOCATION_AGE_MILLIS = 5 * 60 * 1000L

    fun isRecent(locationElapsedRealtimeNanos: Long, nowElapsedRealtimeNanos: Long): Boolean {
        if (locationElapsedRealtimeNanos <= 0L || nowElapsedRealtimeNanos < locationElapsedRealtimeNanos) {
            return false
        }

        val ageMillis = (nowElapsedRealtimeNanos - locationElapsedRealtimeNanos) / NANOS_PER_MILLI
        return ageMillis <= MAX_CACHED_LOCATION_AGE_MILLIS
    }
}
