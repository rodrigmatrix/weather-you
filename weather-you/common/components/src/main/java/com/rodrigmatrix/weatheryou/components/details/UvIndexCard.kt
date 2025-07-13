package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.R
import androidx.constraintlayout.compose.ConstraintLayout
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_10_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_1_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_2_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_3_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_4_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_5_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_6_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_7_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_8_uv_index_color
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.color.level_9_uv_index_color
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexAlertStringRes
import com.rodrigmatrix.weatheryou.components.details.extensions.uvIndexStringRes
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour

@Composable
fun UvIndexCardContent(
    uvIndex: Double,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(200.dp)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_sunny),
                    contentDescription = stringResource(R.string.uv_index),
                    tint = WeatherYouTheme.colorScheme.weatherTextColor,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = stringResource(R.string.uv_index),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleMedium,
                )
            }
            Text(
                text = uvIndex.toInt().toString(),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.titleMedium
            )
            Text(
                text = stringResource(uvIndex.toInt().uvIndexStringRes()),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.titleMedium
            )
        }
        UvIndexBar(uvIndex)
        Text(
            text = stringResource(uvIndex.toInt().uvIndexAlertStringRes()),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UvIndexBar(
    uvIndex: Double,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (uvDot, uvBar) = createRefs()
        val biasFloat: Float by animateFloatAsState(
            targetValue = ((uvIndex / 10f).toFloat()),
            animationSpec = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            label = "",
        )
        Box(
            modifier
                .padding(start = 4.dp, end = 4.dp)
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = getUvIndexGradientList()
                    )
                )
                .constrainAs(uvBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Icon(
            painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_dot),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(16.dp)
                .constrainAs(uvDot) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        bias = biasFloat
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        bias = 0.5f
                    )
                }
        )
    }
}

fun List<Double>.toUvIndexGradientList(): List<Color> {
    return this.map {
        when (it.toInt()) {
            in (0..0) -> level_1_uv_index_color
            in (1..1) -> level_2_uv_index_color
            in (2..2) -> level_3_uv_index_color
            in (3..4) -> level_4_uv_index_color
            in (4..5) -> level_5_uv_index_color
            in (5..6) -> level_6_uv_index_color
            in (6..7) -> level_6_uv_index_color
            in (8..9) -> level_8_uv_index_color
            in (9..10) -> level_9_uv_index_color
            else -> level_10_uv_index_color
        }
    }
}


fun getUvIndexGradient(
    hours: List<WeatherHour>,
): List<Color> {
    val colors = mutableListOf<Color>()
    listOf(
        hours.minOf { it.uvIndex },
        hours.find { it.dateTime.hourOfDay == 12 }?.uvIndex ?: 0.0,
        hours.maxOf { it.uvIndex },
    ).forEach {
        when (it.toInt()) {
            in (0..0) -> colors.add(level_1_uv_index_color)
            in (1..1) -> colors.add(level_2_uv_index_color)
            in (2..2) -> colors.add(level_3_uv_index_color)
            in (3..4) -> colors.add(level_4_uv_index_color)
            in (4..5) -> colors.add(level_5_uv_index_color)
            in (5..6) -> colors.add(level_6_uv_index_color)
            in (6..7) -> colors.add(level_7_uv_index_color)
            in (8..9) -> colors.add(level_8_uv_index_color)
            in (9..10) -> colors.add(level_9_uv_index_color)
            else -> colors.add(level_10_uv_index_color)
        }
    }
    return if (colors.size == 1) {
        colors.add(colors[0])
        colors
    } else if (colors.isEmpty()) {
        listOf(level_1_uv_index_color, level_1_uv_index_color)
    } else {
        colors
    }
}

fun getUvIndexGradientList(): List<Color> {
    return listOf(
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
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UvIndexCardPreview() {
    WeatherYouTheme {
        UvIndexCardContent(
            uvIndex = 5.0,
        )
    }
}