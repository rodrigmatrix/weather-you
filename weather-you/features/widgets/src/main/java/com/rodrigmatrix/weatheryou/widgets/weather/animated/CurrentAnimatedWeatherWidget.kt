package com.rodrigmatrix.weatheryou.widgets.weather.animated

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import com.rodrigmatrix.weatheryou.widgets.weather.MediumLargeLoading
import com.rodrigmatrix.weatheryou.widgets.weather.WidgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentAnimatedWeatherWidget: GlanceAppWidget(), KoinComponent {

    companion object {
        private val smallMode = DpSize(100.dp, 184.dp)
        private val smallLarge = DpSize(260.dp, 184.dp)
        private val mediumMode = DpSize(270.dp, 250.dp)
        private val largeMode = DpSize(270.dp, 280.dp)
        private val superLargeMode = DpSize(270.dp, 300.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(smallMode, smallLarge, mediumMode, largeMode, superLargeMode)
    )

    private val getWidgetTemperatureUseCase by inject<GetWidgetTemperatureUseCase>()

    private val deleteWidgetLocationUseCase by inject<DeleteWidgetLocationUseCase>()

    private val scope = CoroutineScope(SupervisorJob())

    private var widgetState by mutableStateOf<WidgetState>(WidgetState.Loading)

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id).toString()
        provideContent {
            val size = LocalSize.current
            GlanceTheme {
                when (widgetState) {
                    is WidgetState.Complete -> {
                        when (size) {
                            smallMode -> AnimatedSmallWidget(
                                weather = (widgetState as WidgetState.Complete).weather,
                                width = size.width.value,
                                height = size.height.value,
                                onWidgetClicked = { openMainActivity(context, (widgetState as WidgetState.Complete).weather) },
                            )
                            smallLarge, mediumMode, largeMode -> AnimatedMediumLargeWidget(
                                weather = (widgetState as WidgetState.Complete).weather,
                                showDays = size.height >= largeMode.height,
                                daysCount = 4,
                                width = size.width.value,
                                height = size.height.value,
                                onWidgetClicked = { openMainActivity(context, (widgetState as WidgetState.Complete).weather) },
                            )
                            superLargeMode -> AnimatedMediumLargeWidget(
                                weather = (widgetState as WidgetState.Complete).weather,
                                showDays = size.height >= largeMode.height,
                                width = size.width.value,
                                height = size.height.value,
                                daysCount = 8,
                                onWidgetClicked = { openMainActivity(context, (widgetState as WidgetState.Complete).weather) },
                            )
                        }
                    }
                    WidgetState.Error -> {

                    }
                    WidgetState.Loading -> {
                        MediumLargeLoading(
                            onWidgetClicked = { openMainActivity(context, null) },
                        )
                    }
                }
            }
            LaunchedEffect(size) { updateWeather(appWidgetId) }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId).toString()
        scope.launch {
            deleteWidgetLocationUseCase(widgetId).firstOrNull()
            super.onDelete(context, glanceId)
        }
    }

    fun updateWeather(appWidgetId: String) {
        scope.launch {
            widgetState = WidgetState.Loading
            getWidgetTemperatureUseCase(appWidgetId)
                .firstOrNull { weather ->
                    widgetState = if (weather != null) {
                        WidgetState.Complete(weather)
                    } else {
                        WidgetState.Error
                    }
                    return@firstOrNull true
                }
        }
    }

    private fun openMainActivity(
        context: Context,
        weatherLocation: WeatherLocation?,
    ) {
        Intent(Intent.ACTION_VIEW).apply {
            setData(Uri.parse("weatheryou.rodrigmatrix.com"))
            weatherLocation?.let {
                putExtra("latitude", weatherLocation.latitude)
                putExtra("longitude", weatherLocation.longitude)
            }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }
}