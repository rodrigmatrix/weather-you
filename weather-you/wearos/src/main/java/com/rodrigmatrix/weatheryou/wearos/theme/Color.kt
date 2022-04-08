package com.rodrigmatrix.weatheryou.wearos.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

private val Primary = Color(0xFFb4c4ff)
private val Secondary = Color(0xFFb4c5ff)
private val Error = Color(0xFFCF6679)

val MaxTemperatureColor = Color(0xFFF53C70)
val LowestTemperatureColor = Primary

internal val WearColorPalette: Colors = Colors(
    primary = Primary,
    primaryVariant = Primary,
    secondary = Secondary,
    secondaryVariant = Secondary,
    error = Error,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)