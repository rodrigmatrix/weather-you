package com.rodrigmatrix.weatheryou.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouCard

@Composable
fun WindCard(
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
                        painter = painterResource(R.drawable.ic_water_drop),
                        contentDescription = stringResource(R.string.humidity),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(R.string.humidity),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Text(
                    text = "87%",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Text(
                text = "The dew point is 22 right now.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}