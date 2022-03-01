package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons

class VisualCrossingWeatherIconMapper {

    fun map(source: String): WeatherIcons {
        return when(source) {
            "partly-cloudy-day" -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_cloudy,
                staticIcon = R.drawable.ic_weather_partly_cloudy
            )

            "partly-cloudy-night" -> WeatherIcons(
                animatedIcon = R.raw.weather_cloudynight,
                staticIcon = R.drawable.ic_weather_cloudynight
            )

            "cloudy" -> WeatherIcons(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            "wind" -> WeatherIcons(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            "clear-day"-> WeatherIcons(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )

            "clear-night" -> WeatherIcons(
                animatedIcon = R.raw.weather_night,
                staticIcon = R.drawable.ic_weather_night
            )

            "snow" -> WeatherIcons(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            "snow-night" -> WeatherIcons(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            "rain" -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_shower,
                staticIcon = R.drawable.ic_weather_partly_shower
            )

            "fog" -> WeatherIcons(
                animatedIcon = R.raw.weather_mist,
                staticIcon = R.drawable.ic_weather_mist
            )

            "snow-showers-day" -> WeatherIcons(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            "snow-showers-night" -> WeatherIcons(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            "thunder-rain" -> WeatherIcons(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )

            "thunder-showers-day" -> WeatherIcons(
                animatedIcon = R.raw.weather_stormshowersday,
                staticIcon = R.drawable.ic_weather_stormshowersday
            )

            "thunder-showers-night" -> WeatherIcons(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )

            else -> WeatherIcons(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )
        }
    }
}