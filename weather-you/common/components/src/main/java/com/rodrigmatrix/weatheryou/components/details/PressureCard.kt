package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.core.extensions.pressureString
import com.rodrigmatrix.weatheryou.core.extensions.pressureUnitString
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.domain.model.PressureTrend
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PressureCardContent(
    pressure: Double,
    trend: PressureTrend,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_pressure),
                contentDescription = stringResource(R.string.pressure),
                tint = WeatherYouTheme.colorScheme.weatherTextColor,
                modifier = Modifier.padding(end = 4.dp),
            )
            Text(
                text = stringResource(R.string.pressure),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.titleMedium,
            )
        }
        Spacer(Modifier.height(4.dp))
        PressureGauge(
            pressure = pressure,
            trend = trend,
            modifier = Modifier
                .fillMaxWidth()
                .size(140.dp),
        )
    }
}

@Composable
fun PressureGauge(
    pressure: Double,
    trend: PressureTrend,
    modifier: Modifier = Modifier,
    minPressure: Int = 962,
    maxPressure: Int = 1062
) {
    var animate by remember { mutableStateOf(false) }

    val pressureRatio = remember(pressure, minPressure, maxPressure) {
        ((pressure - minPressure).toFloat() / (maxPressure - minPressure).toFloat()).coerceIn(0f, 1f)
    }

    val animatedPressureRatio by animateFloatAsState(
        targetValue = if (animate) pressureRatio else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "PressureAnimation"
    )

    LaunchedEffect(Unit) {
        animate = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val indicatorColor = WeatherYouTheme.colorScheme.tertiary
        val textColor = WeatherYouTheme.colorScheme.weatherTextColor
        val pointsColor = WeatherYouTheme.colorScheme.primary

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(
                    when (trend) {
                        PressureTrend.Rising -> com.rodrigmatrix.weatheryou.components.R.drawable.ic_arrow_warmup
                        PressureTrend.Falling -> com.rodrigmatrix.weatheryou.components.R.drawable.ic_arrow_cooldown
                        PressureTrend.Steady -> com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_equal
                    }
                ),
                contentDescription = stringResource(R.string.pressure),
                tint = textColor,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = pressure.pressureString(),
                style = WeatherYouTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = textColor,
            )
            Text(
                text = pressure.pressureUnitString(),
                style = WeatherYouTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = textColor,
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = (minOf(size.width, size.height) / 2) * 0.85f
            val center = this.center
            val totalTicks = 50
            val startAngle = 135f
            val sweepAngle = 270f
            val tickLength = radius * 0.12f
            val indicatorLength = radius * 0.22f

            val animatedPressureAngle = startAngle + animatedPressureRatio * sweepAngle
            val animatedPressureTickIndex = (animatedPressureRatio * totalTicks).toInt()

            for (i in 0..totalTicks) {
                val tickAngle = startAngle + (i.toFloat() / totalTicks) * sweepAngle
                val tickAngleRad = Math.toRadians(tickAngle.toDouble()).toFloat()

                val startOffset = Offset(
                    x = center.x + (radius - tickLength) * cos(tickAngleRad),
                    y = center.y + (radius - tickLength) * sin(tickAngleRad)
                )
                val endOffset = Offset(
                    x = center.x + radius * cos(tickAngleRad),
                    y = center.y + radius * sin(tickAngleRad)
                )

                val gradientTicks = 15
                val baseAlpha = 0.4f

                val color = when (trend) {
                    PressureTrend.Rising -> {
                        if (i <= animatedPressureTickIndex) {
                            val distance = animatedPressureTickIndex - i
                            if (distance < gradientTicks) {
                                val alpha =
                                    baseAlpha + (1f - baseAlpha) * (1f - distance.toFloat() / gradientTicks)
                                pointsColor.copy(alpha = alpha)
                            } else {
                                pointsColor.copy(alpha = baseAlpha)
                            }
                        } else {
                            pointsColor.copy(alpha = baseAlpha)
                        }
                    }

                    PressureTrend.Falling -> {
                        if (i > animatedPressureTickIndex) {
                            val distance = i - animatedPressureTickIndex
                            if (distance < gradientTicks) {
                                val alpha =
                                    baseAlpha + (1f - baseAlpha) * (1f - distance.toFloat() / gradientTicks)
                                pointsColor.copy(alpha = alpha)
                            } else {
                                pointsColor.copy(alpha = baseAlpha)
                            }
                        } else {
                            pointsColor.copy(alpha = baseAlpha)
                        }
                    }

                    PressureTrend.Steady -> {
                        pointsColor.copy(alpha = baseAlpha)
                    }
                }

                drawLine(
                    color = color,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }

            val indicatorAngleRad = Math.toRadians(animatedPressureAngle.toDouble()).toFloat()
            val indicatorStartOffset = Offset(
                x = center.x + (radius - indicatorLength) * cos(indicatorAngleRad),
                y = center.y + (radius - indicatorLength) * sin(indicatorAngleRad)
            )
            val indicatorEndOffset = Offset(
                x = center.x + radius * cos(indicatorAngleRad),
                y = center.y + radius * sin(indicatorAngleRad)
            )
            drawLine(
                color = indicatorColor,
                start = indicatorStartOffset,
                end = indicatorEndOffset,
                strokeWidth = 10f,
                cap = StrokeCap.Round,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.low),
                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.high),
                style = WeatherYouTheme.typography.bodyMedium,
                color = WeatherYouTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "Trends")
@Preview(name = "Trends Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PressureCardTrendsPreview() {
    WeatherYouTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PressureCardContent(
                pressure = 995.0,
                trend = PressureTrend.Falling,
                modifier = Modifier.weight(1f)
            )
            PressureCardContent(
                pressure = 1010.0,
                trend = PressureTrend.Steady,
                modifier = Modifier.weight(1f)
            )
            PressureCardContent(
                pressure = 1025.0,
                trend = PressureTrend.Rising,
                modifier = Modifier.weight(1f)
            )
        }
    }
}