package com.rodrigmatrix.weatheryou.components.particle

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

@Composable
fun WeatherAnimationsBackground(
    weatherLocation: WeatherLocation,
    particleTick: Long,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = weatherLocation,
        animationSpec = tween(durationMillis = 1200),
        modifier = modifier,
    ) { targetWeatherLocation ->
        val gradientList = targetWeatherLocation.getGradientList()
        val contentModifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientList))

        var cloudCount = 0
        var starCount = 20
        var cloudsFraction = (targetWeatherLocation.cloudCover / 100.0).toFloat()
        var starsFraction = 0.25f
        var isRaining = false
        var isSnowing = false
        var isHailing = false
        var isThunderstorm = false

        when (targetWeatherLocation.currentCondition) {
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
                // No specific particle changes
            }
            WeatherCondition.Breezy -> {
                // No specific particle changes
            }
            WeatherCondition.Windy -> {
                starCount = 20
            }
            WeatherCondition.Drizzle -> {
                starCount = 20
            }
            WeatherCondition.HeavyRain -> {
                cloudCount = 25
                // rainIntensity = 1f // Not used directly in private fun call
                isRaining = true
            }
            WeatherCondition.Rain -> {
                cloudCount = 20
                // rainIntensity = 0.5f // Not used directly in private fun call
                isRaining = true
            }
            WeatherCondition.Showers -> {
                cloudCount = 20
                // rainIntensity = 0.5f // Not used directly in private fun call
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
                // rainIntensity = 0.5f
                starCount = 0
                cloudCount = 20
            }
            WeatherCondition.MixedRainAndSnow -> {
                isRaining = true
                // rainIntensity = 0.5f
                starCount = 0
                cloudCount = 20
                isSnowing = true
            }
            WeatherCondition.MixedRainfall -> { // Potentially ambiguous, default to some clouds
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
                // isRaining = true; // Was previously set, ensure if intended
                isSnowing = true
            }
            WeatherCondition.Sleet -> {
                // No specific particle changes, consider if it should have hail/snow
            }
            WeatherCondition.Snow -> {
                isSnowing = true
                cloudCount = 5
            }
            WeatherCondition.SnowShowers -> {
                // isRaining = true; // Was previously set, ensure if intended
                isSnowing = true
            }
            WeatherCondition.Blizzard -> {
                isSnowing = true
                cloudCount = 150 // More clouds for blizzard
                cloudsFraction = 1f
            }
            WeatherCondition.BlowingSnow -> {
                isSnowing = true
            }
            WeatherCondition.FreezingDrizzle -> {
                isSnowing = true // Or isHailing = true for ice pellets
            }
            WeatherCondition.FreezingRain -> {
                isRaining = true
                isHailing = true
            }
            WeatherCondition.Frigid -> {
                // No specific particle changes
            }
            WeatherCondition.Hail -> {
                isHailing = true
            }
            WeatherCondition.Hot -> {
                // No specific particle changes
            }
            WeatherCondition.Hurricane -> {
                 isRaining = true
                 isThunderstorm = true // Hurricanes often have thunderstorms
                 cloudCount = 200
                 cloudsFraction = 1f
            }
            WeatherCondition.IsolatedThunderstorms -> {
                isThunderstorm = true
            }
            WeatherCondition.SevereThunderstorm -> {
                isThunderstorm = true
                isRaining = true
                cloudCount = 150
            }
            WeatherCondition.Thunderstorm, WeatherCondition.Thunderstorms -> {
                isThunderstorm = true
                cloudCount = 150
                isRaining = true
            }
            WeatherCondition.Tornado -> {
                cloudCount = 200 // Dense clouds
                cloudsFraction = 1f
                // Could add a specific tornado visual if available
            }
            WeatherCondition.TropicalStorm -> {
                isRaining = true
                cloudCount = 100
                // Consider if thunderstorms are typical
            }
        }

        WeatherAnimationsBackground(
            particle = particleTick,
            modifier = contentModifier, // Use the new contentModifier
            cloudsFraction = cloudsFraction,
            starsFraction = starsFraction,
            // rainIntensity = rainIntensity, // rainIntensity is not a param of the private fun
            cloudCount = cloudCount,
            starsCount = starCount,
            isRaining = isRaining,
            isSnowing = isSnowing,
            isThunderstorm = isThunderstorm,
            isHailing = isHailing,
            isDaylight = targetWeatherLocation.isDaylight,
        )
    }
}

@Composable
private fun WeatherAnimationsBackground(
    particle: Long,
    modifier: Modifier = Modifier,
    cloudsFraction: Float,
    starsFraction: Float,
    // rainIntensity: Float, // This parameter does not exist
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
               width = 400, // Consider making these dynamic or passed as params
               height = 600
           )
       }
       if (isRaining) {
           // The rainIntensity parameter was removed from the private function,
           // so we use a default or derive it if needed inside Particles
           Particles(
               iteration = particle,
               parameters = rainParameters, // If rainParameters needs intensity, it must be part of the object
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
