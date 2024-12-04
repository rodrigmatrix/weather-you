package com.rodrigmatrix.weatheryou.components.particle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.android.awaitFrame

@Composable
fun WeatherAnimationsBackground(
    weatherLocation: WeatherLocation,
    modifier: Modifier = Modifier,
) {
    //val particle = fps().longValue
    var particle by rememberSaveable { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            val nextFrame = awaitFrame() / 100_000L
            particle = nextFrame
        }
    }

    key(weatherLocation) {
        val gradientList = weatherLocation.getGradientList()
        val modifierWithBackground = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientList))
        var cloudCount = 0
        var starCount = 20
        var cloudsFraction = (weatherLocation.cloudCover / 100.0).toFloat()
        var starsFraction = 0.25f
        var rainIntensity = 0.0f
        var isRaining = false
        var isSnowing = false
        var isHailing = false
        var isThunderstorm = false
        when (weatherLocation.currentCondition) {
            WeatherCondition.Clear -> {
                starCount = 50
            }
            WeatherCondition.Cloudy -> {
                cloudCount = 150
                starCount = 0
                cloudsFraction = 1f
            }
            WeatherCondition.Dust -> {
                starCount = 0
            }
            WeatherCondition.Fog -> {
                cloudCount = 150
                starCount = 0
                cloudsFraction = 1f
            }
            WeatherCondition.Haze -> {
                starCount = 0
            }
            WeatherCondition.MostlyClear -> {
                cloudCount = 3
                cloudsFraction = 0.10f
                starCount = 20
            }
            WeatherCondition.MostlyCloudy -> {
                cloudCount = 100
                starCount = 10
                cloudsFraction = 0.40f
            }
            WeatherCondition.PartlyCloudy -> {
                cloudCount = 50
                cloudsFraction = 0.30f
            }
            WeatherCondition.ScatteredThunderstorms -> {
                isThunderstorm = true
            }
            WeatherCondition.Smoke -> {

            }
            WeatherCondition.Breezy -> {

            }
            WeatherCondition.Windy -> {
                starCount = 20
            }
            WeatherCondition.Drizzle -> {
                starCount = 20
            }
            WeatherCondition.HeavyRain -> {
                cloudCount = 25
                rainIntensity = 1f
                isRaining = true
            }
            WeatherCondition.Rain -> {
                cloudCount = 20
                rainIntensity = 0.5f
                isRaining = true
            }
            WeatherCondition.Showers -> {
                // clouds have light colors
                cloudCount = 20
                rainIntensity = 0.5f
                isRaining = true
            }
            WeatherCondition.Flurries -> {
                isSnowing = true
            }
            WeatherCondition.HeavySnow -> {
                isSnowing = true
                starCount = 0
                cloudCount = 20
            }
            WeatherCondition.MixedRainAndSleet -> {
                isRaining = true
                rainIntensity = 0.5f
                starCount = 0
                cloudCount = 20
            }
            WeatherCondition.MixedRainAndSnow -> {
                isRaining = true
                rainIntensity = 0.5f
                starCount = 0
                cloudCount = 20
                isSnowing = true
            }
            WeatherCondition.MixedRainfall -> {
                starCount = 0
                cloudCount = 20
            }
            WeatherCondition.MixedSnowAndSleet -> {
                isSnowing = true
            }
            WeatherCondition.ScatteredShowers -> {
                isRaining = true
            }
            WeatherCondition.ScatteredSnowShowers -> {
                isRaining = true
                isSnowing = true
            }
            WeatherCondition.Sleet -> {

            }
            WeatherCondition.Snow -> {
                isSnowing = true
                cloudCount = 5
            }
            WeatherCondition.SnowShowers -> {
                isRaining = true
                isSnowing = true
            }
            WeatherCondition.Blizzard -> {
                isSnowing = true
            }
            WeatherCondition.BlowingSnow -> {
                isSnowing = true
            }
            WeatherCondition.FreezingDrizzle -> {
                isSnowing = true
            }
            WeatherCondition.FreezingRain -> {
                isRaining = true
                isHailing = true
            }
            WeatherCondition.Frigid -> {

            }
            WeatherCondition.Hail -> {
                isHailing = true
            }
            WeatherCondition.Hot -> {

            }
            WeatherCondition.Hurricane -> {

            }
            WeatherCondition.IsolatedThunderstorms -> {
                isThunderstorm = true
            }
            WeatherCondition.SevereThunderstorm -> {
                isThunderstorm = true
                isRaining = true
            }
            WeatherCondition.Thunderstorm, WeatherCondition.Thunderstorms -> {
                isThunderstorm = true
                cloudCount = 150
                isRaining = true
            }
            WeatherCondition.Tornado -> {

            }
            WeatherCondition.TropicalStorm -> {

            }
        }

        WeatherAnimationsBackground(
            particle = particle,
            modifier = modifierWithBackground,
            cloudsFraction = cloudsFraction,
            starsFraction = starsFraction,
            rainIntensity = rainIntensity,
            cloudCount = cloudCount,
            starsCount = starCount,
            isRaining = isRaining,
            isSnowing = isSnowing,
            isThunderstorm = isThunderstorm,
            isHailing = isHailing,
            isDaylight = weatherLocation.isDaylight,
        )
    }
}

@Composable
private fun WeatherAnimationsBackground(
    particle: Long,
    modifier: Modifier = Modifier,
    cloudsFraction: Float,
    starsFraction: Float,
    rainIntensity: Float,
    cloudCount: Int,
    starsCount: Int,
    isRaining: Boolean,
    isSnowing: Boolean,
    isThunderstorm: Boolean,
    isHailing: Boolean,
    isDaylight: Boolean,
) {
   Box(modifier = modifier) {
       if (starsCount > 0 && !isDaylight) {
           Particles(
               iteration = particle,
               parameters = starsParameters.copy(particleCount = starsCount),
               blinkAnimation = true,
               modifier = Modifier
                   .fillMaxSize()
                   .fillMaxHeight(fraction = starsFraction),
           )
       }
       if (cloudCount > 0) {
           Clouds(
               tint = ColorFilter.tint(
                   Color.Black.copy(alpha = 0.2f),
                   BlendMode.SrcAtop
               ),
               particleAnimationIteration = particle,
               cloudCount = cloudCount,
               modifier = Modifier
                   .fillMaxHeight(fraction = cloudsFraction)
                   .fillMaxSize()
           )
       }
       if (isThunderstorm) {
           Thunder(
               particleAnimationIteration = particle,
               width = 400,
               height = 600
           )
       }
       if (isRaining) {
           Particles(
               iteration = particle,
               parameters = rainParameters,
           )
       }
       if (isHailing) {
           Particles(
               iteration = particle,
               parameters = hailParameters,
           )
       }
       if (isSnowing) {
           Particles(
               iteration = particle,
               parameters = snowParameters,
           )
       }
   }
}