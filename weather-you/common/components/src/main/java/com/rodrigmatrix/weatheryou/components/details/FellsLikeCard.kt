package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigmatrix.weatheryou.components.extensions.toGradientList
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.domain.R
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun FeelsLikeCardContent(
    feelsLikeTemp: Double,
    actualTemp: Double,
    modifier: Modifier = Modifier
) {
    val tempDifference = feelsLikeTemp - actualTemp
    val description = when {
        tempDifference < -0.5 -> stringResource(R.string.feels_like_cooler_desc)
        tempDifference > 0.5 -> stringResource(R.string.feels_like_warmer_desc)
        else -> stringResource(R.string.feels_like_same_desc)
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .height(170.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                painter = painterResource(com.rodrigmatrix.weatheryou.components.R.drawable.ic_thermostat),
                contentDescription = stringResource(R.string.feels_like),
                tint = WeatherYouTheme.colorScheme.weatherTextColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.feels_like),
                style = WeatherYouTheme.typography.titleMedium,
                color = WeatherYouTheme.colorScheme.weatherTextColor
            )
        }
        Text(
            text = "${feelsLikeTemp.roundToInt()}°",
            style = WeatherYouTheme.typography.titleLarge,
            color = WeatherYouTheme.colorScheme.weatherTextColor
        )
        Text(
            text = stringResource(R.string.actual_temperature, actualTemp.roundToInt()),
            style = WeatherYouTheme.typography.titleMedium,
            color = WeatherYouTheme.colorScheme.weatherTextColor.copy(alpha = 0.8f)
        )
        Spacer(Modifier.height(16.dp))
        TemperatureDifferenceSlider(
            actualTemp = actualTemp,
            feelsLikeTemp = feelsLikeTemp
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = description,
            style = WeatherYouTheme.typography.bodyMedium,
            autoSize = TextAutoSize.StepBased(
                minFontSize = 8.sp,
            ),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            maxLines = 2,
        )
    }
}

@Composable
fun TemperatureDifferenceSlider(
    actualTemp: Double,
    feelsLikeTemp: Double,
    modifier: Modifier = Modifier
) {
    val difference = feelsLikeTemp - actualTemp
    if (abs(difference) <= 2) {
        return
    }

    val isCooler = difference < 0
    val textMeasurer = rememberTextMeasurer()
    val diffText = remember { AnnotatedString("${abs(difference).roundToInt()}°") }
    val textStyle = WeatherYouTheme.typography.bodySmall.copy(color = Color.DarkGray)
    val textLayoutResult = remember(diffText, textStyle) {
        textMeasurer.measure(diffText, textStyle)
    }
    val icon = if (isCooler) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val iconPainter = rememberVectorPainter(image = icon)
    val trackColor = WeatherYouTheme.colorScheme.weatherTextColor.copy(alpha = 0.2f)
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val yCenter = canvasHeight / 2

        val trackStrokeWidth = 8.dp.toPx()
        val pillWidth = 46.dp.toPx()
        val pillHeight = canvasHeight

        val gradientColors = listOf(feelsLikeTemp, actualTemp).toGradientList()

        val lengthRatio = (abs(difference) / 10f).coerceIn(0.0, 1.0)

        // Draw base track
        drawLine(
            color = trackColor,
            start = Offset(x = 0f, y = yCenter),
            end = Offset(x = canvasWidth, y = yCenter),
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )

        val currentGradient = if (isCooler) gradientColors else gradientColors.asReversed()
        val barStart: Float
        var pillCenterX: Float

        if (isCooler) {
            barStart = canvasWidth
            pillCenterX = (canvasWidth * (1f - lengthRatio)).toFloat()
        } else {
            barStart = 0f
            pillCenterX = (canvasWidth * lengthRatio).toFloat()
        }

        val pillRadiusX = pillWidth / 2f
        pillCenterX = pillCenterX.coerceIn(pillRadiusX, canvasWidth - pillRadiusX)
        val barEnd = pillCenterX

        // 2. Draw highlighted bar
        drawLine(
            brush = Brush.horizontalGradient(
                colors = currentGradient,
                startX =  if (isCooler) barEnd else barStart,
                endX = if (isCooler) barStart else barEnd,
            ),
            start = Offset(x = barStart, y = yCenter),
            end = Offset(x = barEnd, y = yCenter),
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )

        // 3. Draw pill background
        val pillColor = if (isCooler) currentGradient.first() else currentGradient.last()
        drawRoundRect(
            color = pillColor,
            topLeft = Offset(x = pillCenterX - (pillWidth / 2), y = 0f),
            size = Size(width = pillWidth, height = pillHeight),
            cornerRadius = CornerRadius(x = pillHeight / 2, y = pillHeight / 2),
        )

        // 4. Draw pill content (Icon and Text)
        val iconSizePx = 16.dp.toPx()
        val iconSize = Size(iconSizePx, iconSizePx)
        val contentPadding = 2.dp.toPx()
        val totalContentWidth = iconSize.width + contentPadding + textLayoutResult.size.width
        val contentStartX = pillCenterX - (totalContentWidth / 2f)

        translate(
            left = contentStartX,
            top = yCenter - (iconSize.height / 2f)
        ) {
            with(iconPainter) {
                draw(
                    size = iconSize,
                    colorFilter = ColorFilter.tint(Color.DarkGray)
                )
            }
        }

        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = contentStartX + iconSize.width + contentPadding,
                y = yCenter - (textLayoutResult.size.height / 2f)
            )
        )
    }
}

@Preview(name = "Cooler -4")
@Preview(name = "Cooler -4 Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelsLikeCardPreviewCooler() {
    WeatherYouTheme {
        FeelsLikeCardContent(feelsLikeTemp = 13.0, actualTemp = 16.0)
    }
}

@Preview(name = "Cooler -10")
@Preview(name = "Cooler -10 Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelsLikeCardPreviewCoolerFull() {
    WeatherYouTheme {
        FeelsLikeCardContent(feelsLikeTemp = 0.0, actualTemp = 10.0)
    }
}

@Preview(name = "Warmer +3")
@Preview(name = "Warmer +3 Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelsLikeCardPreviewWarmer() {
    WeatherYouTheme {
        FeelsLikeCardContent(feelsLikeTemp = 32.0, actualTemp = 29.0)
    }
}

@Preview(name = "Hidden (Diff <= 2)")
@Preview(name = "Hidden (Diff <= 2) Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelsLikeCardPreviewHidden() {
    WeatherYouTheme {
        FeelsLikeCardContent(feelsLikeTemp = 21.0, actualTemp = 20.0)
    }
}

@Preview(name = "Hidden (Diff <= 2)")
@Preview(name = "Hidden (Diff <= 2) Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelsLikeCard31PreviewHidden() {
    WeatherYouTheme {
        FeelsLikeCardContent(feelsLikeTemp = 31.0, actualTemp = 20.0)
    }
}