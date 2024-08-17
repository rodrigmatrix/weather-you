package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
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

    private val mainScope: CoroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val size = LocalSize.current
            val state by getWidgetTemperatureUseCase().collectAsState(null)
            GlanceTheme {
                if (state == null) {
                    MediumLargeLoading(
                        onWidgetClicked = { openMainActivity(context) },
                    )
                } else {
                    state?.let { weather ->
                        when (size) {
                            smallMode -> SmallWidget(
                                weather = weather,
                                onWidgetClicked = { openMainActivity(context) },
                            )

                            mediumMode, largeMode -> MediumLargeWidget(
                                weather = weather,
                                showDays = size.height >= largeMode.height,
                                onWidgetClicked = { openMainActivity(context) },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun openMainActivity(context: Context) {
        Intent("action.weatheryou.open").setPackage(context.packageName).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun updateWidget(context: Context) {
        mainScope.launch {
            updateAll(context)
        }
    }
}