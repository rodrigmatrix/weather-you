package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherAnimatedWidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = CurrentAnimatedWeatherWidget()
}