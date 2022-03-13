package com.rodrigmatrix.weatheryou.widgets.weather.small

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.rodrigmatrix.weatheryou.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherSmallWidget: GlanceAppWidget(), KoinComponent {

    private val context by inject<Context>()

    private val mainScope: CoroutineScope = MainScope()

    @Composable
    override fun Content() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.weather_small_wdiget_background))
        ) {
            Text(
                text = "12°C",
                style = TextStyle(
                    color = ColorProvider(Color.White), fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(20.dp))
            Image(
                provider = ImageProvider(com.rodrigmatrix.weatheryou.data.R.drawable.ic_weather_cloudynight),
                contentDescription = null,
                modifier = GlanceModifier.size(40.dp)
            )
        }
    }

    fun updateWidget() {
        mainScope.launch {
            updateAll(context)
        }
    }
}