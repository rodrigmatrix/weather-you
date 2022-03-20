package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString
import com.rodrigmatrix.weatheryou.wearos.R
import com.rodrigmatrix.weatheryou.wearos.theme.LowestTemperatureColor
import com.rodrigmatrix.weatheryou.wearos.theme.MaxTemperatureColor

@Composable
fun MaxAndLowestWeather(
    max: Double,
    lowest: Double,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_upward),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = MaxTemperatureColor
        )
        Text(
            text = max.temperatureString(),
            style = MaterialTheme.typography.caption2,
            color = MaxTemperatureColor
        )
        Spacer(Modifier.padding(end = 8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_downward),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = LowestTemperatureColor
        )
        Text(
            text = lowest.temperatureString(),
            style = MaterialTheme.typography.caption2,
            color = LowestTemperatureColor
        )
    }
}