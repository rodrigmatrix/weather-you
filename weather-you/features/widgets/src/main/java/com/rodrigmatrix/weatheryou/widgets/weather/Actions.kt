package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.glance.action.Action
import androidx.glance.appwidget.action.actionStartActivity
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

fun openMainActivity(
    context: Context,
    weatherLocation: WeatherLocation?,
): Action {
    val isDebuggable = 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    val packageName = if (isDebuggable) {
        "com.rodrigmatrix.weatheryou.debug"
    } else {
        "com.rodrigmatrix.weatheryou"
    }
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
        weatherLocation?.let {
            putExtra("latitude", weatherLocation.latitude)
            putExtra("longitude", weatherLocation.longitude)
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }!!
    return actionStartActivity(intent)
}