package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.wearos.theme.*
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.presentation.extension.uvIndexStringRes

@Composable
fun UvIndex(
    uvIndex: Double,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
    ) {
        val (uvDot, uvBar, uvIndexText, uvIcon) = createRefs()
        val biasFloat: Float by animateFloatAsState(
            targetValue = ((uvIndex / 10.0).toFloat()),
            animationSpec = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            )
        )
        Box(
            modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(brush = Brush.horizontalGradient(colors = UnIndexGradientColorList))
                .constrainAs(uvBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(uvIcon) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                linkTo(
                    top = parent.top,
                    bottom = uvBar.top,
                    bottomMargin = 10.dp
                )
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sunny),
                contentDescription = stringResource(R.string.uv_index),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = stringResource(R.string.uv_index),
                style = MaterialTheme.typography.title3
            )
        }
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
                        bias = biasFloat
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        bias = 0.5f
                    )
                }
        )
        Text(
            text = stringResource(uvIndex.toInt().uvIndexStringRes()),
            style = MaterialTheme.typography.title3,
            modifier = Modifier
                .constrainAs(uvIndexText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    linkTo(
                        top = uvBar.bottom, topMargin = 10.dp,
                        bottom = parent.bottom
                    )
                }
        )
    }
}