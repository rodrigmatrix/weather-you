package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.MoonPhase
import com.rodrigmatrix.weatheryou.domain.model.PrecipitationType

fun String?.toPrecipitationType(): PrecipitationType {
    return try {
        when (this) {
            "Clear" -> PrecipitationType.Clear
            "Precipitation" -> PrecipitationType.Precipitation
            "Rain" -> PrecipitationType.Rain
            "Snow" -> PrecipitationType.Snow
            "Sleet" -> PrecipitationType.Sleet
            "Hail" -> PrecipitationType.Hail
            "Mixed" -> PrecipitationType.Mixed
            else -> PrecipitationType.Clear
        }
    } catch (_: Exception) {
        PrecipitationType.Clear
    }
}

fun String?.toMoonPhase(): MoonPhase {
    return try {
        MoonPhase.valueOf(this.orEmpty())
    } catch (_: Exception) {
        MoonPhase.New
    }
}