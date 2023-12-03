package com.rodrigmatrix.weatheryou.widgets.weather

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.rodrigmatrix.weatheryou.domain.usecase.UpdateWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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
            val size = LocalSize.current
            val state by getWidgetTemperatureUseCase()
                .catch {
                    it
                }
                .collectAsState(initial = null)
            GlanceTheme {
                if (state == null) {
                    when (size) {
                        smallMode -> Unit

                        mediumMode, largeMode -> MediumWidgetLoading(
                            onWidgetClicked =   actionStartActivity<Activity>()
                        )
                    }
                } else {
                    state?.let { weather ->
                        when (size) {
                            smallMode -> SmallWidget(
                                weather = weather,
                                onWidgetClicked = actionStartActivity<Activity>()
                            )

                            mediumMode, largeMode -> MediumWidget(
                                weather = weather,
                                showDays = size.height >= largeMode.height,
                                onWidgetClicked = actionStartActivity<Activity>()
                            )
                        }
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