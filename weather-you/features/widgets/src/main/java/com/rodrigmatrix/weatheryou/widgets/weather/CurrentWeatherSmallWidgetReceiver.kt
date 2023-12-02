package com.rodrigmatrix.weatheryou.widgets.weather

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class CurrentWeatherSmallWidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = CurrentWeatherSmallWidget()
}