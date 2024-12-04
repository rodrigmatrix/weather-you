package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherWidget: GlanceAppWidget(), KoinComponent {

    companion object {
        private val smallMode = DpSize(260.dp, 184.dp)
        private val mediumMode = DpSize(270.dp, 200.dp)
        private val largeMode = DpSize(270.dp, 280.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(smallMode, mediumMode, largeMode)
    )

    private val getWidgetTemperatureUseCase by inject<GetWidgetTemperatureUseCase>()

    private val deleteWidgetLocationUseCase by inject<DeleteWidgetLocationUseCase>()

    private val mainScope: CoroutineScope = MainScope()



    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val appWidgetId = LocalGlanceId.current.toString().filter { it.isDigit() }.toString()
            var widgetState by remember {
                mutableStateOf<WidgetState>(WidgetState.Loading)
            }
            val size = LocalSize.current
            GlanceTheme {
                when (widgetState) {
                    is WidgetState.Complete -> {
                        when (size) {
                            smallMode -> SmallWidget(
                                weather = (widgetState as WidgetState.Complete).weather,
                                onWidgetClicked = { openMainActivity(context) },
                            )
                            mediumMode, largeMode -> MediumLargeWidget(
                                weather = (widgetState as WidgetState.Complete).weather,
                                showDays = size.height >= largeMode.height,
                                onWidgetClicked = { openMainActivity(context) },
                            )
                        }
                    }
                    WidgetState.Error -> {

                    }
                    WidgetState.Loading -> {
                        MediumLargeLoading(
                            onWidgetClicked = { openMainActivity(context) },
                        )
                    }
                }
                LaunchedEffect(Unit) {
                    getWidgetTemperatureUseCase(appWidgetId)
                        .onStart {
                            widgetState = WidgetState.Loading
                        }
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
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId).toString()
        mainScope.launch {
            deleteWidgetLocationUseCase(widgetId)
                .catch {
                    super.onDelete(context, glanceId)
                }
                .collectLatest {
                    super.onDelete(context, glanceId)
                }
        }
    }

    private fun openMainActivity(context: Context) {
        Intent("action.weatheryou.open").setPackage(context.packageName).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }
}