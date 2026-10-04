@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodrigmatrix.weatheryou.core.state

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference
import com.rodrigmatrix.weatheryou.domain.model.AppSettings
import com.rodrigmatrix.weatheryou.domain.model.AppThemePreference
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference

val LocalWeatherYouAppSettings = compositionLocalOf {
    AppSettings.DEFAULT
}

val LocalWeatherYouCurrentDestination = compositionLocalOf {
    ""
}

@OptIn(ExperimentalMaterial3Api::class)
val LocalWeatherYouConditionsScaffoldState = compositionLocalOf<SheetState?> {
    null
}

@OptIn(ExperimentalMaterial3Api::class)
val LocalWeatherYouParticleState = compositionLocalOf<State<Long>> {
    androidx.compose.runtime.mutableStateOf(0L)
}

@Composable
fun WeatherYouAppState(
    appSettings: AppSettings,
    currentDestination: String,
    conditionsScaffoldState: SheetState?,
    particleTick: State<Long> = androidx.compose.runtime.mutableStateOf(0L),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWeatherYouAppSettings provides appSettings,
        LocalWeatherYouCurrentDestination provides currentDestination,
        LocalWeatherYouConditionsScaffoldState provides conditionsScaffoldState,
        LocalWeatherYouParticleState provides particleTick,
    ) {
        content()
    }
}

object WeatherYouAppState {

    val appSettings: AppSettings
        @Composable
        get() = LocalWeatherYouAppSettings.current

    val currentDestination: String
        @Composable
        get() = LocalWeatherYouCurrentDestination.current

    @OptIn(ExperimentalMaterial3Api::class)
    val conditionsScaffoldState: SheetState?
        @Composable
        get() = LocalWeatherYouConditionsScaffoldState.current

    val particleTick: State<Long>
        @Composable
        get() = LocalWeatherYouParticleState.current
}
