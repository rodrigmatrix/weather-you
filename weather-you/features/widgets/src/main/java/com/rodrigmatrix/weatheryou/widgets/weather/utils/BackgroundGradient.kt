package com.rodrigmatrix.weatheryou.widgets.weather.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.ui.graphics.toArgb
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

fun WeatherLocation.gradientBackground(
    width: Float,
    height: Float,
): Bitmap {
    val colors = getGradientList().map {
        it.toArgb()
    }.toIntArray()
    val painter = Paint().apply {
        isDither = true
        setShader(LinearGradient(0f, 0f, 0f, height, colors,null, Shader.TileMode.CLAMP))
    }

    val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawRect(RectF(0f, 0f, width, height), painter)
    return bitmap
}