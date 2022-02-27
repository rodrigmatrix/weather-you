package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.presentation.extensions.uvIndexAlertStringRes
import com.rodrigmatrix.weatheryou.presentation.extensions.uvIndexStringRes
import com.rodrigmatrix.weatheryou.presentation.theme.*

@Composable
fun UvIndexCard(
    uvIndex: Double,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier.height(200.dp)) {
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
                        contentDescription = stringResource(R.string.humidity),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(R.string.uv_index),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Text(
                    text = uvIndex.toInt().toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(uvIndex.toInt().uvIndexStringRes()),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            UvIndexBar(uvIndex)
            Text(
                text = stringResource(uvIndex.toInt().uvIndexAlertStringRes()),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun UvIndexBar(
    uvIndex: Double,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (uvDot, uvBar) = createRefs()
        Box(
            modifier
                .padding(start = 4.dp, end = 4.dp)
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
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
            painter = painterResource(R.drawable.ic_dot),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(16.dp)
                .constrainAs(uvDot) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        bias = (uvIndex / 10.0).toFloat()
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UvIndexCardPreview() {
    WeatherYouTheme {
        UvIndexCard(
            uvIndex = 5.0,
        )
    }
}