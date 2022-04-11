package com.rodrigmatrix.weatheryou.data.analytics

import android.os.Bundle

internal interface WeatherYouAnalytics {

    fun sendEvent(name: String, params: Bundle? = null)
}