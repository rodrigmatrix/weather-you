package com.rodrigmatrix.weatheryou.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class WeatherYouAnalyticsImpl(
    private val firebaseAnalytics: FirebaseAnalytics
) : WeatherYouAnalytics {

    override fun sendEvent(name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name, params)
    }
}