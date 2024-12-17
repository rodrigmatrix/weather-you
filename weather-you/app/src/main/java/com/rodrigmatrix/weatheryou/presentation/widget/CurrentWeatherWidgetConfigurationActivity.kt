package com.rodrigmatrix.weatheryou.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.widgets.weather.CurrentWeatherWidget
import com.rodrigmatrix.weatheryou.widgets.weather.animated.CurrentAnimatedWeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentWeatherWidgetConfigurationActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = intent
            val extras = intent.extras
            val widgetId = (extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            ) ?: AppWidgetManager.INVALID_APPWIDGET_ID)
            WeatherYouTheme {
                CurrentWeatherWidgetConfigurationScreen(
                    widgetId = widgetId.toString(),
                    onConfigurationCancelled = {
                        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                        setResult(RESULT_CANCELED, resultValue)
                        finish()
                    },
                    onConfigurationComplete = {
                        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                        lifecycleScope.launch {
                            runCatching {
                                CurrentAnimatedWeatherWidget().updateAll(this@CurrentWeatherWidgetConfigurationActivity)
                                CurrentWeatherWidget().updateAll(this@CurrentWeatherWidgetConfigurationActivity)
                            }
                            setResult(RESULT_OK, resultValue)
                            finish()
                        }
                    }
                )
            }
        }
    }
}