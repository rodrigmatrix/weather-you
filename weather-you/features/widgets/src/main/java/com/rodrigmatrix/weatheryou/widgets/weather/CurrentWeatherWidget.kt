package com.rodrigmatrix.weatheryou.widgets.weather

import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.rodrigmatrix.weatheryou.components.theme.LocalWeatherYouThemeSettingsEnabled
import com.rodrigmatrix.weatheryou.core.state.LocalWeatherYouAppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val getAppSettingsUseCase by inject<GetAppSettingsUseCase>()

    private val deleteWidgetLocationUseCase by inject<DeleteWidgetLocationUseCase>()

    private val mainScope: CoroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val glanceWidgetManager = GlanceAppWidgetManager(context)
        val appWidgetId = glanceWidgetManager.getAppWidgetId(id).toString()
        val weatherFlow = getWidgetTemperatureUseCase(appWidgetId)
            .onStart{ emit(null) }
        val appSettingsFlow = getAppSettingsUseCase()
        provideContent {
            val weather by weatherFlow.collectAsState(null)
            val appSettings by appSettingsFlow.collectAsState(AppSettings.DEFAULT)
            val size = LocalSize.current
            CompositionLocalProvider(
                LocalWeatherYouAppSettings provides appSettings,
            ) {
                GlanceTheme {
                    if (weather != null) {
                        when (size) {
                            smallMode -> SmallWidget(
                                weather = weather!!,
                                onWidgetClicked = openMainActivity(context, weather),
                            )
                            mediumMode, largeMode -> MediumLargeWidget(
                                weather = weather!!,
                                showDays = size.height >= largeMode.height,
                                onWidgetClicked = openMainActivity(context, weather),
                            )
                        }
                    } else {
                        MediumLargeLoading(
                            onWidgetClicked = openMainActivity(context, null),
                        )
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
}