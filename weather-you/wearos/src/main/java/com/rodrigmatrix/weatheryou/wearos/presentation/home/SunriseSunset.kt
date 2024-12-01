package com.rodrigmatrix.weatheryou.wearos.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.rodrigmatrix.weatheryou.core.extensions.getHourWithMinutesString
import com.rodrigmatrix.weatheryou.wearos.R
import org.joda.time.DateTime

@Composable
fun SunriseSunset(
    sunrise: DateTime,
    sunset: DateTime,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_sunny),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(text = stringResource(R.string.sunrise))
        }
        Text(
            text = sunrise.getHourWithMinutesString(context),
            style = MaterialTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Image(
                painter = painterResource(com.rodrigmatrix.weatheryou.weathericons.R.drawable.ic_weather_night),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(text = stringResource(R.string.sunset))
        }
        Text(
            text = sunset.getHourWithMinutesString(context),
            style = MaterialTheme.typography.title1
        )
    }
}