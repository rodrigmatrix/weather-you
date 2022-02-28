package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.theme.day_color
import com.rodrigmatrix.weatheryou.presentation.theme.future_color
import com.rodrigmatrix.weatheryou.presentation.theme.night_color


@Composable
fun SunriseSunsetCard(
    sunriseHour: Int,
    sunsetHour: Int,
    currentHour: Int,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sunny),
                        contentDescription = stringResource(R.string.sunrise_sunset),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(R.string.sunrise_sunset),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            SunriseSunsetVisualizer(
                sunriseHour = sunriseHour,
                sunsetHour = sunsetHour,
                currentHour = currentHour,
                modifier = Modifier.height(140.dp)
            )
        }
    }
}

@Composable
private fun SunriseSunsetVisualizer(
    sunriseHour: Int,
    sunsetHour: Int,
    currentHour: Int,
    modifier: Modifier = Modifier
) {
    val colorPrimary = MaterialTheme.colorScheme.primary
    val tertiaryPrimary = MaterialTheme.colorScheme.tertiary
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val dayStart = ((sunriseHour / 24f))
        val dayEnd = ((sunsetHour / 24f))
        val scaleX = canvasWidth / 23f
        val scaleY = canvasHeight / 2f
        var interval = (dayEnd - dayStart) / 2
        var interval2 = (1 - dayEnd + dayStart) / 2
        var start = dayStart - (1 - dayEnd + dayStart)
        interval *= 24 * scaleX
        interval2 *= 24 * scaleX
        start *= 24 * scaleX

        // horizon line
        drawLine(
            color = colorPrimary,
            start = Offset(0f, canvasHeight / 2),
            end = Offset(canvasWidth, canvasHeight / 2),
            strokeWidth = 1f
        )
        // sun
        if (currentHour in sunriseHour..sunsetHour) {
            drawCircle(
                color = Color.Yellow,
                radius = 20f,
                center = Offset(
                    x = scaleX * currentHour,
                    y = scaleY * ((interval2 / interval + 1))
                )
            )
        }
        // sunrise line
        drawLine(
            color = colorPrimary,
            start = Offset(interval2, canvasHeight / 2),
            end = Offset(interval2, (canvasHeight * 25) / 100),
            strokeWidth = 1f
        )
        drawIntoCanvas {
            val canvas = it.nativeCanvas
            val sunrisePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = colorPrimary.toArgb()
            }
            val sunsetPaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = tertiaryPrimary.toArgb()
            }
            val futurePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = Color.Transparent.toArgb()
            }
            val path = android.graphics.Path().apply {
                moveTo(start, scaleY)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1) / 2), interval2 * 2, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1) / 2), interval * 2, 0f)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1) / 2), interval2 * 2, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1) / 2), interval * 2, 0f)
            }
            canvas.clipPath(path)
            canvas.drawRect(0f, 0f, scaleX * currentHour, scaleY, sunrisePaint)
            canvas.drawRect(0f, scaleY, scaleX * currentHour, canvasHeight, sunsetPaint)
            canvas.drawRect(scaleX * currentHour, 0f, canvasWidth, canvasHeight, futurePaint)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SunriseSunsetCardPreview() {
    WeatherYouTheme {
        Column {
            SunriseSunsetCard(
                sunriseHour = 6,
                sunsetHour = 17,
                currentHour = 7
            )
            SunriseSunsetCard(
                sunriseHour = 6,
                sunsetHour = 18,
                currentHour = 6
            )
        }
    }
}