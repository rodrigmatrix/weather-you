package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcon

class WeatherIconMapper {

    fun map(source: String): WeatherIcon {
        return when(source) {
            "partly-cloudy-day" -> WeatherIcon(
                animatedIcon = R.raw.weather_partly_cloudy,
                staticIcon = R.drawable.ic_weather_partly_cloudy
            )

            "partly-cloudy-night" -> WeatherIcon(
                animatedIcon = R.raw.weather_cloudynight,
                staticIcon = R.drawable.ic_weather_cloudynight
            )

            "cloudy" -> WeatherIcon(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            "wind" -> WeatherIcon(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            "clear-day"-> WeatherIcon(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )

            "clear-night" -> WeatherIcon(
                animatedIcon = R.raw.weather_night,
                staticIcon = R.drawable.ic_weather_night
            )

            "snow" -> WeatherIcon(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            "snow-night" -> WeatherIcon(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            "rain" -> WeatherIcon(
                animatedIcon = R.raw.weather_partly_shower,
                staticIcon = R.drawable.ic_weather_partly_shower
            )

            "fog" -> WeatherIcon(
                animatedIcon = R.raw.weather_mist,
                staticIcon = R.drawable.ic_weather_mist
            )

            "snow-showers-day" -> WeatherIcon(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            "snow-showers-night" -> WeatherIcon(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            "thunder-rain" -> WeatherIcon(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )

            "thunder-showers-day" -> WeatherIcon(
                animatedIcon = R.raw.weather_stormshowersday,
                staticIcon = R.drawable.ic_weather_stormshowersday
            )

            "thunder-showers-night" -> WeatherIcon(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )

            else -> WeatherIcon(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )
        }
    }
}