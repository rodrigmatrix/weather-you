package com.rodrigmatrix.weatheryou.widgets.weather.animated

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
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.rodrigmatrix.weatheryou.core.state.LocalWeatherYouAppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.usecase.DeleteWidgetLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetWidgetTemperatureUseCase
import com.rodrigmatrix.weatheryou.widgets.weather.MediumLargeLoading
import com.rodrigmatrix.weatheryou.widgets.weather.openMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class CurrentAnimatedWeatherWidget(

): GlanceAppWidget(), KoinComponent {

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

    private val deleteWidgetLocationUseCase by inject<DeleteWidgetLocationUseCase>()

    private val getAppSettingsUseCase by inject<GetAppSettingsUseCase>()

    private val getWidgetTemperatureUseCase by inject<GetWidgetTemperatureUseCase>()

    private val scope = CoroutineScope(SupervisorJob())

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val glanceWidgetManager = GlanceAppWidgetManager(context)
        val appWidgetId = glanceWidgetManager.getAppWidgetId(id).toString()
        val weatherFlow = getWidgetTemperatureUseCase(appWidgetId)
            .onStart { emit(null) }
        val appSettingsFlow = getAppSettingsUseCase()
        provideContent {
            val weather by weatherFlow.collectAsState(null)
            val appSettings by appSettingsFlow.collectAsState(AppSettings(
                temperaturePreference = TemperaturePreference.METRIC,
                enableWeatherAnimations = false,
                enableThemeColorWithWeatherAnimations = false,
                appColorPreference = AppColorPreference.DEFAULT,
                appThemePreference = AppThemePreference.SYSTEM_DEFAULT,
            ))
            val size = LocalSize.current
            CompositionLocalProvider(
                LocalWeatherYouAppSettings provides appSettings,
            ) {
                GlanceTheme {
                    if (weather != null) {
                        when (size) {
                            smallMode -> AnimatedSmallWidget(
                                weather = weather!!,
                                width = size.width.value,
                                height = size.height.value,
                                onWidgetClicked = openMainActivity(context, weather),
                            )
                            smallLarge, mediumMode, largeMode -> AnimatedMediumLargeWidget(
                                weather = weather!!,
                                showDays = size.height >= largeMode.height,
                                daysCount = 4,
                                width = size.width.value,
                                height = size.height.value,
                                onWidgetClicked = openMainActivity(context, weather),
                            )
                            superLargeMode -> AnimatedMediumLargeWidget(
                                weather = weather!!,
                                showDays = size.height >= largeMode.height,
                                width = size.width.value,
                                height = size.height.value,
                                daysCount = 8,
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
        scope.launch {
            deleteWidgetLocationUseCase(widgetId).firstOrNull()
            super.onDelete(context, glanceId)
        }
    }
}