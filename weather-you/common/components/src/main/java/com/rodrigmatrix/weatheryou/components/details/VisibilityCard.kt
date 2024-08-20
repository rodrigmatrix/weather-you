package com.rodrigmatrix.weatheryou.components.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import com.rodrigmatrix.weatheryou.components.theme.WeatherYouTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.core.extensions.speedString
import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.extensions.visibilityConditionsStringRes

@Composable
fun VisibilityCardContent(
    visibility: Double,
    unit: TemperaturePreference,
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
                    painter = painterResource(
                        com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_visibility
                    ),
                    contentDescription = stringResource(R.string.visibility),
                    tint = WeatherYouTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = stringResource(R.string.visibility),
                    color = WeatherYouTheme.colorScheme.onSecondaryContainer,
                    style = WeatherYouTheme.typography.titleMedium,
                )
            }
            Text(
                text = visibility.speedString(unit),
                color = WeatherYouTheme.colorScheme.onSecondaryContainer,
                style = WeatherYouTheme.typography.titleLarge
            )
        }
        Text(
            text = stringResource(visibility.visibilityConditionsStringRes()),
            color = WeatherYouTheme.colorScheme.onSecondaryContainer,
            style = WeatherYouTheme.typography.bodyLarge
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VisibilityCardPreview() {
    WeatherYouTheme {
        VisibilityCardContent(
            visibility = 80.0,
            unit = TemperaturePreference.METRIC,
        )
    }
}