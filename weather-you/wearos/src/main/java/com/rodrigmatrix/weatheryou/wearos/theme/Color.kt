package com.rodrigmatrix.weatheryou.wearos.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

private val Primary = Color(0xFFb4c4ff)
private val Secondary = Color(0xFFb4c5ff)
private val Error = Color(0xFFCF6679)

val MaxTemperatureColor = Color(0xFFF53C70)
val LowestTemperatureColor = Primary

val level_1_uv_index_color = Color(0xff41d925)
val level_2_uv_index_color = Color(0xffafcc23)
val level_3_uv_index_color = Color(0xffd6c422)
val level_4_uv_index_color = Color(0xffd39420)
val level_5_uv_index_color = Color(0xffd26c20)
val level_6_uv_index_color = Color(0xffcd4722)
val level_7_uv_index_color = Color(0xffc90d1f)
val level_8_uv_index_color = Color(0xffbd0c1e)
val level_9_uv_index_color = Color(0xffcd1365)
val level_10_uv_index_color = Color(0xffcd188d)

val UnIndexGradientColorList = listOf(
    level_1_uv_index_color,
    level_2_uv_index_color,
    level_3_uv_index_color,
    level_4_uv_index_color,
    level_5_uv_index_color,
    level_6_uv_index_color,
    level_7_uv_index_color,
    level_8_uv_index_color,
    level_9_uv_index_color,
    level_10_uv_index_color
)

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