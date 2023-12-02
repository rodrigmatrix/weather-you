package com.rodrigmatrix.weatheryou.widgets.weather

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherSmallWidget: GlanceAppWidget(), KoinComponent {

    companion object {
        private val smallMode = DpSize(260.dp, 184.dp)
        private val mediumMode = DpSize(270.dp, 200.dp)
        private val largeMode = DpSize(270.dp, 280.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(smallMode, mediumMode, largeMode)
    )

    private val context by inject<Context>()

    private val getWidgetTemperatureUseCase by inject<GetWidgetTemperatureUseCase>()

    private val mainScope: CoroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state by getWidgetTemperatureUseCase().collectAsState(initial = null)
            GlanceTheme {
                val size = LocalSize.current
                state?.let {
                    when (size) {
                        smallMode -> SmallWidget(
                            it,
                            actionStartActivity<Activity>()
                        )

                        mediumMode -> MediumWidget(
                            it,
                            actionStartActivity<Activity>()
                        )
                        largeMode -> MediumWidget(
                            it,
                            actionStartActivity<Activity>()
                        )
                    }
                }
            }
        }
    }

    fun updateWidget() {
        mainScope.launch {
            updateAll(context)
        }
    }
}