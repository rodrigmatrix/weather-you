package com.rodrigmatrix.weatheryou.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.components.extensions.getAnimatedIcon
import com.rodrigmatrix.weatheryou.components.extensions.getStaticIcon
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

@Composable
fun WeatherIcon(
    weatherCondition: WeatherCondition,
    isDaylight: Boolean,
    modifier: Modifier = Modifier,
    alwaysStatic: Boolean = false,
    contentDescription: String? = null
) {
    if (alwaysStatic.not()) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(weatherCondition.getAnimatedIcon(isDaylight))
        )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(weatherCondition.getStaticIcon(isDaylight)),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}