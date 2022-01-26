package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.R

class WeatherIconMapper {

    fun map(source: String): Int {
        return when(source) {
            "partly-cloudy-day" -> R.raw.weather_partly_cloudy

            "partly-cloudy-night" -> R.raw.weather_cloudynight

            "cloudy" -> R.raw.weather_windy

            "wind" -> R.raw.weather_windy

            "clear-day" -> R.raw.weather_sunny

            "clear-night" -> R.raw.weather_night

            "snow" -> R.raw.weather_snow

            "snow-night" -> R.raw.weather_snownight

            "rain" -> R.raw.weather_partly_shower

            "fog" -> R.raw.weather_mist

            "snow-showers-day" -> R.raw.weather_snow

            "snow-showers-night" -> R.raw.weather_snownight

            "thunder-rain" -> R.raw.weather_storm

            "thunder-showers-day" -> R.raw.weather_stormshowersday

            "thunder-showers-night" -> R.raw.weather_storm

            else -> R.raw.weather_sunny
        }
    }
}