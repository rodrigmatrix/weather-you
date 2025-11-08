package com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.PopupValue
import kotlin.math.roundToInt

@Composable
fun ChartPopupHeader(
    popupValue: PopupValue,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var popupWidthPx by remember { mutableIntStateOf(0) }

    val screenWidthPx = remember(configuration, density) {
        with(density) { configuration.screenWidthDp.dp.toPx() }
    }

    val marginPx = with(density) { 16.dp.toPx() }

    val xOffset = remember(popupValue.position.x, popupWidthPx, screenWidthPx) {
        if (popupWidthPx == 0) return@remember 0f

        val centeredX = popupValue.position.x - (popupWidthPx / 2f)

        val minBound = marginPx
        val maxBound = screenWidthPx - popupWidthPx - marginPx

        centeredX.coerceIn(minBound, maxBound)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .offset { IntOffset(xOffset.roundToInt(), 0) }
            .onGloballyPositioned { coordinates ->
                popupWidthPx = coordinates.size.width
            }
    ) {
        content()
    }
}