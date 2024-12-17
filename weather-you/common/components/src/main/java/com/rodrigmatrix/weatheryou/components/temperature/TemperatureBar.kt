package com.rodrigmatrix.weatheryou.components.temperature

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.extensions.getTemperatureGradient
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import kotlin.math.absoluteValue

@Composable
fun TemperatureBar(
    minWeekTemperature: Double,
    maxWeekTemperature: Double,
    minDayTemperature: Double,
    maxDayTemperature: Double,
    hours: List<WeatherHour>,
    currentTemperature: Double?,
    modifier: Modifier = Modifier,
) {
    val gradientList = getTemperatureGradient(
        minDayTemperature = minDayTemperature,
        maxDayTemperature = maxDayTemperature,
        hours = hours,
    )
    val backgroundColor = Color.Black.copy(alpha = 0.1f)
    Canvas(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .height(6.dp)
    ) {
        val width = this.size.width
        val height = this.size.height
        val valueIncrease = ((width / (maxWeekTemperature - minWeekTemperature))).toFloat()
        val startPosition = valueIncrease * (minWeekTemperature - minDayTemperature).absoluteValue
        val endPosition = ((width / (maxWeekTemperature - minWeekTemperature)) * (maxDayTemperature - minWeekTemperature))
        val currentPosition = if (currentTemperature != null) {
            val slope = (endPosition - startPosition) / (maxDayTemperature - minDayTemperature)
            val intercept = startPosition - (slope * minDayTemperature)
            slope * currentTemperature + intercept
        } else {
            0.0
        }

        drawLine(
            brush = Brush.horizontalGradient(gradientList),
            start = Offset(startPosition.toFloat(), height / 2),
            end = Offset(endPosition.toFloat(), height / 2),
            strokeWidth = 20f,
            cap = StrokeCap.Round,
        )
        if (currentTemperature != null) {
            drawCircle(
                color = backgroundColor,
                radius = 9f,
                center = Offset(currentPosition.toFloat(), height / 2),
                style = Stroke(width = 3f),
            )
            drawCircle(
                color = Color.White,
                radius = 8f,
                center = Offset(currentPosition.toFloat(), height / 2),
            )
            drawCircle(
                color = backgroundColor,
                radius = 9f,
                center = Offset(currentPosition.toFloat(), height / 2),
                style = Stroke(width = 3f),
            )
        }
    }
}

@Preview
@Composable
private fun TemperatureBarPreview() {
    WeatherYouTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(Color(0xFF5DB6AB))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            TemperatureBar(
                minWeekTemperature = 14.0,
                maxWeekTemperature = 34.0,
                minDayTemperature = 14.0,
                hours = emptyList(),
                maxDayTemperature = 24.0,
                currentTemperature = 20.0,
                modifier = Modifier.fillMaxWidth(),
            )
            TemperatureBar(
                minWeekTemperature = 11.0,
                maxWeekTemperature = 26.0,
                minDayTemperature = 13.0,
                hours = emptyList(),
                maxDayTemperature = 12.0,
                currentTemperature = 12.0,
                modifier = Modifier.fillMaxWidth(),
            )

            TemperatureBar(
                minWeekTemperature = 11.0,
                maxWeekTemperature = 26.0,
                minDayTemperature = 13.0,
                maxDayTemperature = 15.0,
                hours = emptyList(),
                currentTemperature = 13.0,
                modifier = Modifier.fillMaxWidth(),
            )

            TemperatureBar(
                minWeekTemperature = 14.0,
                maxWeekTemperature = 34.0,
                minDayTemperature = 18.0,
                maxDayTemperature = 34.0,
                hours = emptyList(),
                currentTemperature = 34.0,
                modifier = Modifier.fillMaxWidth(),
            )

            TemperatureBar(
                minWeekTemperature = 14.0,
                maxWeekTemperature = 34.0,
                minDayTemperature = 14.0,
                maxDayTemperature = 23.0,
                hours = emptyList(),
                currentTemperature = 16.0,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}