package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherAnimatedWidgetReceiver: GlanceAppWidgetReceiver(), KoinComponent {

    override val glanceAppWidget: GlanceAppWidget = CurrentAnimatedWeatherWidget()

    private val coroutineScope = MainScope()

    private val getWidgetTemperatureUseCase by inject<GetWidgetTemperatureUseCase>()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        coroutineScope.launch(Dispatchers.IO) {
            val glanceWidgetManager = GlanceAppWidgetManager(context)
            updateWidgetState(context, glanceWidgetManager.getGlanceIdBy(appWidgetId))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun observeData(context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            val glanceWidgetManager = GlanceAppWidgetManager(context)
            glanceWidgetManager.getGlanceIds(
                CurrentAnimatedWeatherWidget::class.java
            ).forEach { widgetId ->
                updateWidgetState(context, widgetId)
            }
        }
    }

    private suspend fun updateWidgetState(context: Context, widgetId: GlanceId) {
        val glanceWidgetManager = GlanceAppWidgetManager(context)
        val appWidgetId = glanceWidgetManager.getAppWidgetId(widgetId).toString()
        val weather = getWidgetTemperatureUseCase(appWidgetId)
            .firstOrNull()
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, widgetId) { pref ->
            pref.toMutablePreferences().apply {
                this[currentWeather] = weather.toString()
            }
        }
        glanceAppWidget.update(context, widgetId)
    }

    companion object {
        val currentWeather = stringPreferencesKey("currentWeather")
    }
}