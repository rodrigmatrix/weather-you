package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.components.theme.weatherTextColor
import com.rodrigmatrix.weatheryou.weathericons.R as WeatherIcons
import com.rodrigmatrix.weatheryou.core.extensions.percentageString
import com.rodrigmatrix.weatheryou.core.extensions.temperatureString

@Composable
fun HumidityCardContent(
    humidity: Double,
    dewPoint: Double,
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
                    painter = painterResource(WeatherIcons.drawable.ic_water_drop),
                    contentDescription = stringResource(R.string.humidity),
                    tint = WeatherYouTheme.colorScheme.weatherTextColor,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = stringResource(R.string.humidity),
                    color = WeatherYouTheme.colorScheme.weatherTextColor,
                    style = WeatherYouTheme.typography.titleMedium,
                )
            }
            Text(
                text = humidity.percentageString(),
                color = WeatherYouTheme.colorScheme.weatherTextColor,
                style = WeatherYouTheme.typography.titleLarge
            )
        }
        Text(
            text = stringResource(R.string.the_dew_point_is_x, dewPoint.temperatureString()),
            color = WeatherYouTheme.colorScheme.weatherTextColor,
            style = WeatherYouTheme.typography.bodyLarge
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HumidityCardPreview() {
    WeatherYouTheme {
        HumidityCardContent(
            humidity = 80.0,
            dewPoint = 22.0
        )
    }
}