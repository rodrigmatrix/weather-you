package com.rodrigmatrix.weatheryou.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rodrigmatrix.weatheryou.core.state.WeatherYouAppState
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.DistanceUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationUnitPreference
import com.rodrigmatrix.weatheryou.domain.model.PressureUnitPreference
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
        TemperaturePreference.KELVIN -> (this + 273.15).roundToInt().toString() + "°"
    }
}

fun Double.percentageString(): String {
    return try {
        this.roundToInt().toString() + "%"
    } catch (_: Exception) {
        "${this}%"
    }
}

fun Double.convertSpeed(
    preference: WindUnitPreference,
): String {
    return when (preference) {
        WindUnitPreference.KPH -> this.roundToInt().toString()
        WindUnitPreference.MPH -> (this * 0.621371).roundToInt().toString()
        WindUnitPreference.MS -> (this * 3.6).roundToInt().toString()
        WindUnitPreference.KN -> (this * 1.852).roundToInt().toString()
    }
}

@Composable
fun Double.speedString(
    preference: WindUnitPreference = WeatherYouAppState.appSettings.windUnitPreference,
): String {
    return convertSpeed(preference) + " " + speedUnitString(preference)
}

@Composable
fun speedUnitString(
    preference: WindUnitPreference = WeatherYouAppState.appSettings.windUnitPreference,
): String {
    return when (preference) {
        WindUnitPreference.KPH -> stringResource(R.string.kilometers_per_hour)
        WindUnitPreference.MPH -> stringResource(R.string.miles_per_hour)
        WindUnitPreference.MS -> stringResource(R.string.meters_per_second)
        WindUnitPreference.KN -> stringResource(R.string.knots)
    }
}

@Composable
fun Double.pressureString(
    preference: PressureUnitPreference = WeatherYouAppState.appSettings.pressureUnitPreference,
): String {
    return when (preference) {
        PressureUnitPreference.MBAR -> "%,d".format(this.roundToInt())
        PressureUnitPreference.HPA -> "%,d".format(this.roundToInt())
        PressureUnitPreference.INHG -> "%.2f".format(this * 0.02953)
        PressureUnitPreference.MMHG -> "%.1f".format(this * 0.750062)
        PressureUnitPreference.KPA -> "%.2f".format(this * 0.1)
    }
}

@Composable
fun Double.pressureUnitString(
    preference: PressureUnitPreference = WeatherYouAppState.appSettings.pressureUnitPreference,
): String {
    return when (preference) {
        PressureUnitPreference.MBAR -> stringResource(R.string.milibars)
        PressureUnitPreference.HPA -> stringResource(R.string.hectopascals)
        PressureUnitPreference.INHG -> stringResource(R.string.inches_of_mercury)
        PressureUnitPreference.MMHG -> stringResource(R.string.millimeters_of_mercury)
        PressureUnitPreference.KPA -> stringResource(R.string.kilopascals)
    }
}

@Composable
fun Double.windDirectionIndicator(): String {
    val directions = listOf(
        R.string.wind_dir_n, R.string.wind_dir_nne, R.string.wind_dir_ne, R.string.wind_dir_ene,
        R.string.wind_dir_e, R.string.wind_dir_ese, R.string.wind_dir_se, R.string.wind_dir_sse,
        R.string.wind_dir_s, R.string.wind_dir_ssw, R.string.wind_dir_sw, R.string.wind_dir_wsw,
        R.string.wind_dir_w, R.string.wind_dir_wnw, R.string.wind_dir_nw, R.string.wind_dir_nnw
    )
    val index = (((this.roundToInt() % 360) + 11.25) / 22.5).toInt() % 16
    return stringResource(id = directions[index])
}


@Composable
fun Double.precipitationString(
    precipitationType: PrecipitationType,
    preference: PrecipitationUnitPreference = WeatherYouAppState.appSettings.precipitationUnitPreference,
): String {
    return when(preference) {
        PrecipitationUnitPreference.MM_CM -> when {
            this < 10 -> this.roundToInt().toString() + " " + stringResource(R.string.milimeters)
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