package com.rodrigmatrix.weatheryou.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcon

@Composable
fun WeatherIcon(
    weatherIcon: WeatherIcon,
    modifier: Modifier = Modifier,
    alwaysStatic: Boolean = false,
    contentDescription: String? = null
) {
    if ((weatherIcon.animatedIcon != 0) && alwaysStatic.not()) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(weatherIcon.animatedIcon)
        )
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition,
            progress,
            modifier = modifier,
        )
    } else {
        Image(
            painter = painterResource(weatherIcon.staticIcon),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}