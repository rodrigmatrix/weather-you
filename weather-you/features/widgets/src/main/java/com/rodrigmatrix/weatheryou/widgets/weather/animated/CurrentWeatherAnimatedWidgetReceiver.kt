package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateLocationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherAnimatedWidgetReceiver: GlanceAppWidgetReceiver(), KoinComponent {

    override val glanceAppWidget: GlanceAppWidget = CurrentAnimatedWeatherWidget()

    private val coroutineScope = MainScope()
    private val updateLocationsUseCase by inject<UpdateLocationsUseCase>()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateLocations(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> updateLocations(context)
        }
    }

    private fun updateLocations(context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                // Fetch fresh weather data first
                updateLocationsUseCase(forceUpdate = true)
                    .flowOn(Dispatchers.IO)
                    .firstOrNull()
                
                // Then update all widgets
                glanceAppWidget.updateAll(context)
            } catch (e: Exception) {
                // Even if data fetch fails, try to update with cached data
                try {
                    glanceAppWidget.updateAll(context)
                } catch (updateError: Exception) {
                }
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        // Update the widget when options change
        coroutineScope.launch(Dispatchers.IO) {
            try {
                glanceAppWidget.updateAll(context)
            } catch (e: Exception) {
            }
        }
    }
}