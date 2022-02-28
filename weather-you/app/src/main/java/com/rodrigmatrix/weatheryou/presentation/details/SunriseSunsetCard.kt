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
import com.rodrigmatrix.weatheryou.presentation.extensions.getDayLengthHours
import com.rodrigmatrix.weatheryou.presentation.extensions.getRemainingDaylightHours
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import org.joda.time.LocalTime

@Composable
fun SunriseSunsetCard(
    sunriseHour: LocalTime,
    sunsetHour: LocalTime,
    currentTime: LocalTime,
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
            Column(modifier = Modifier.padding(bottom = 20.dp)) {
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
            Box {
                SunriseSunsetVisualizer(
                    sunriseHour = sunriseHour.hourOfDay,
                    sunsetHour = sunsetHour.hourOfDay,
                    currentHour = currentTime.hourOfDay,
                    modifier = Modifier.height(140.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sunrise),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = sunriseHour.toString("hh:mm aa"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sunset),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = sunsetHour.toString("hh:mm aa"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            val dayLength = sunriseHour.getDayLengthHours(sunsetHour)
            if (dayLength.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.lenght_of_day_x, dayLength),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            val remainingDaylight = currentTime.getRemainingDaylightHours(sunsetHour)
            if (remainingDaylight.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.remaning_daylight_x, remainingDaylight),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
    val secondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
    val tertiaryPrimary = MaterialTheme.colorScheme.tertiary
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val dayStart = ((sunriseHour / 24f))
        val dayEnd = ((sunsetHour / 24f))
        val scaleX = canvasWidth / 24f
        val scaleY = canvasHeight / 2f
        var interval = (dayEnd - dayStart) / 2f
        var interval2 = (1f - dayEnd + dayStart) / 2f
        var start = dayStart - (1f - dayEnd + dayStart)
        interval *= 24f * scaleX
        interval2 *= 24f * scaleX
        start *= 24f * scaleX
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
                radius = 16f,
                center = Offset(
                    x = scaleX * currentHour,
                    y = ((canvasHeight / 2) / ((sunsetHour - sunriseHour) * currentHour))
                )
            )
        }
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
                color = secondaryColor.toArgb()
            }
            val path = android.graphics.Path().apply {
                moveTo(start, scaleY)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1f) / 2f), interval2 * 2f, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1f) / 2f), interval * 2f, 0f)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1f) / 2f), interval2 * 2f, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1f) / 2f), interval * 2f, 0f)
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
                sunriseHour = LocalTime(5, 0),
                sunsetHour = LocalTime(17, 0),
                currentTime = LocalTime(6, 0)
            )
            SunriseSunsetCard(
                sunriseHour = LocalTime(6, 0),
                sunsetHour = LocalTime(17, 0),
                currentTime = LocalTime(6, 0)
            )
        }
    }
}