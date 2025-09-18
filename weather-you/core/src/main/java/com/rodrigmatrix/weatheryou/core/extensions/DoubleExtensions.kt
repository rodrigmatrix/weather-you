package com.rodrigmatrix.weatheryou.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WindUnitPreference
import java.lang.Exception
import kotlin.math.roundToInt

@Composable
fun Double.temperatureString(
    temperaturePreference: TemperaturePreference = WeatherYouAppState.appSettings.temperaturePreference,
): String = toTemperatureString(temperaturePreference)

fun Double.toTemperatureString(
    temperaturePreference: TemperaturePreference,
): String {
    val intTemp = this.roundToInt()
    return when (temperaturePreference) {
        TemperaturePreference.METRIC -> "$intTemp°"
        TemperaturePreference.IMPERIAL -> ((this * 1.8) + 32).roundToInt().toString() + "°"
    }
}

fun Double.percentageString(): String {
    return try {
        this.roundToInt().toString() + "%"
    } catch (_: Exception) {
        "${this}%"
    }
}

@Composable
fun Double.speedString(
    preference: WindUnitPreference = WeatherYouAppState.appSettings.windUnitPreference,
): String {
    return when (preference) {
        WindUnitPreference.KPH -> this.roundToInt().toString() + " " + stringResource(R.string.kilometers_per_hour)
        WindUnitPreference.MPH -> (this * 0.621371).roundToInt().toString() + " " + stringResource(R.string.miles_per_hour)
        WindUnitPreference.MS -> (this * 3.6).roundToInt().toString() + " " + stringResource(R.string.meters_per_second)
        WindUnitPreference.KN -> (this * 1.852).roundToInt().toString() + " " + stringResource(R.string.knots)
    }
}

@Composable
fun Double.precipitationString(
    precipitationType: PrecipitationType,
    preference: PrecipitationUnitPreference = WeatherYouAppState.appSettings.precipitationUnitPreference,
): String {
    return when(preference) {
        PrecipitationUnitPreference.MM_CM -> when {
            this < 10 -> this.roundToInt().toString() + " " + stringResource(R.string.millimeters)
            else -> (this / 10).roundToInt().toString() + " " + stringResource(R.string.centimeters)
        }
        PrecipitationUnitPreference.IN -> (this * 0.0393701).roundToInt().toString() + " " + stringResource(R.string.inches)
    }
}

@Composable
fun Double.distanceString(
    preference: DistanceUnitPreference = WeatherYouAppState.appSettings.distanceUnitPreference,
): String {
    return when (preference) {
        DistanceUnitPreference.KM -> this.roundToInt().toString() + " " + stringResource(R.string.kilometers)
        DistanceUnitPreference.MI -> (this * 0.621371).roundToInt().toString() + " " + stringResource(R.string.miles)
    }
}