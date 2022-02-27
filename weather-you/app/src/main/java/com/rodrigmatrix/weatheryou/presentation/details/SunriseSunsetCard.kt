package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme


@Composable
fun SunriseSunsetCard(
    sunrise: String,
    sunset: String,
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
                sunrise = sunrise,
                sunset = sunset,
                modifier = Modifier.height(160.dp)
            )
        }
    }
}

@Composable
private fun SunriseSunsetVisualizer(
    sunrise: String,
    sunset: String,
    modifier: Modifier = Modifier
) {
    val hour = 7
    val primaryColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier.fillMaxWidth()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val sunriseX = 200f
        val sunsetX = 400f

        val dayStart = 0.25f
        val dayEnd = 0.75f

        val scaleX = canvasWidth / 23f
        val scaleY = canvasHeight / 2f
        var interval: Float = (dayEnd - dayStart) / 2
        var interval2: Float = (1 - dayEnd + dayStart) / 2
        var start: Float = dayStart - (1 - dayEnd + dayStart)
        interval *= 24 * scaleX
        interval2 *= 24 * scaleX
        start *= 24 * scaleX
        // horizon line
        drawLine(
            color = primaryColor,
            start = Offset(0f, canvasHeight / 2),
            end = Offset(canvasWidth, canvasHeight / 2),
            strokeWidth = 4f,
            pathEffect = PathEffect.cornerPathEffect(1f)
        )
        // sunrise line
        drawPath(
            color = primaryColor,
            path = Path().apply {
                moveTo(0f, canvasHeight / 2)
                lineTo(0f, ((canvasHeight * 75) / 100))
//                quadraticBezierTo(interval2, scaleY * ((interval2 / interval + 1) / 2), interval2 * 2, 0f)
//                quadraticBezierTo(interval, -scaleY * ((interval / interval2 + 1) / 2), interval * 2, 0f)
//                quadraticBezierTo(interval2, scaleY * ((interval2 / interval + 1) / 2), interval2 * 2, 0f)
//                quadraticBezierTo(interval, -scaleY * ((interval / interval2 + 1) / 2), interval * 2, 0f)

//                quadraticBezierTo(
//                    x1 = 0f,
//                    y1 = ((canvasHeight * 75) / 100),
//                    x2  = sunriseX,
//                    y2 = canvasHeight / 2
//                )
                close()
            },
            style = Stroke(width = 4f)
        )
        drawPath(
            color = primaryColor,
            path = Path().apply {
                moveTo(0f, ((canvasHeight * 75) / 100))
                drawArc(
                    startAngle = 180f,
                    sweepAngle = 90f,
                    color = primaryColor,
                    useCenter = false,
                    topLeft = Offset(0f, canvasHeight / 2),
                    style = Stroke(3f)
                )
                close()
            },
            style = Stroke(width = 4f)
        )
//        // sun arc
//        drawArc(
//            startAngle = -180f,
//            sweepAngle = 180f,
//            color = primaryColor,
//            useCenter = false,
//            topLeft = Offset(sunriseX, canvasHeight / 2),
//            size = Size(canvasWidth, canvasHeight),
//            style = Stroke(3f)
//        )
        // sun
        drawCircle(
            color = Color.Yellow,
            radius = 40f,
            center = Offset(0f, (canvasWidth / 24) / hour)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SunriseSunsetCardPreview() {
    WeatherYouTheme {
        SunriseSunsetCard(
            sunrise = "",
            sunset = ""
        )
    }
}