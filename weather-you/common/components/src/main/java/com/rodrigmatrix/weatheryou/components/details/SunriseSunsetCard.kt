package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.core.extensions.getHoursAndMinutesDiff
import com.rodrigmatrix.weatheryou.core.extensions.getLocalTime
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import org.joda.time.DateTime
import org.joda.time.LocalTime

@Composable
fun SunriseSunsetCardContent(
    sunrise: DateTime,
    sunset: DateTime,
    currentTime: DateTime,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 10.dp,
            bottom = 10.dp
        )
    ) {
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_sunny),
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
                sunriseHour = sunrise.hourOfDay,
                sunsetHour = sunset.hourOfDay,
                currentHour = currentTime.getLocalTime().hourOfDay,
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
                    text = sunrise.getHourWithMinutesString(context),
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
                    text = sunset.getHourWithMinutesString(context),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        val (dayLengthHours, dayLengthMinutes) = sunrise.getHoursAndMinutesDiff(sunset)
        if (dayLengthHours > 0 || dayLengthMinutes > 0) {
            val dayLengthString = if (dayLengthHours > 0) {
                stringResource(
                    R.string.hours_minutes_x_y,
                    dayLengthHours,
                    dayLengthMinutes
                )
            } else {
                stringResource(R.string.minutes_x, dayLengthMinutes)
            }
            Text(
                text = stringResource(R.string.length_of_day) + dayLengthString,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        val (remainingHours, remainingMinutes) = currentTime.getHoursAndMinutesDiff(sunset)
        if (remainingHours > 0 || remainingMinutes > 0) {
            val remainingDaylightString = if (remainingHours > 0) {
                stringResource(R.string.hours_minutes_x_y, remainingHours, remainingMinutes)
            } else {
                 stringResource(R.string.minutes_x, dayLengthMinutes)
            }
            Text(
                text = stringResource(R.string.remaining_daylight_x) + remainingDaylightString,
                style = MaterialTheme.typography.bodyMedium
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
        // sun
        if (currentHour in sunriseHour..sunsetHour) {
            val daylightHalf = (sunsetHour - sunriseHour)
            val sunScaleY = if (currentHour >= daylightHalf) {
                scaleY - (currentHour - sunsetHour)
            } else {
                scaleY + (currentHour / 24) * 2
            }
            drawCircle(
                color = Color.Yellow,
                radius = 30f,
                center = Offset(
                    x = scaleX * currentHour,
                    y = sunScaleY / 2,
                )
            )
        }
        // horizon line
        drawLine(
            color = colorPrimary,
            start = Offset(0f, canvasHeight / 2),
            end = Offset(canvasWidth, canvasHeight / 2),
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
                color = secondaryColor.toArgb()
            }
            val path = Path().apply {
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
            WeatherYouCard {
                SunriseSunsetCardContent(
                    sunset = LocalTime(6, 30).toDateTimeToday(),
                    sunrise = LocalTime(17, 30).toDateTimeToday(),
                    currentTime = LocalTime(17, 30).toDateTimeToday()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            WeatherYouCard {
                SunriseSunsetVisualizer(
                    sunriseHour = 5,
                    sunsetHour = 17,
                    currentHour = 5,
                )
            }
        }
    }
}