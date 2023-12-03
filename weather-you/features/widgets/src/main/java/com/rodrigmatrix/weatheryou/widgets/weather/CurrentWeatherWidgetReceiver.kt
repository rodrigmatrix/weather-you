package com.rodrigmatrix.weatheryou.widgets.weather

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class CurrentWeatherWidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = CurrentWeatherWidget()
}