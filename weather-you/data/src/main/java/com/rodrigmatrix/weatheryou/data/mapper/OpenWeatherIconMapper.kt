package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.R
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherWeather
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons

class OpenWeatherIconMapper {

    fun map(source: OpenWeatherWeather?): WeatherIcons {
        return when(source?.id ?: 0.0) {
            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_cloudy,
                staticIcon = R.drawable.ic_weather_partly_cloudy
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_cloudynight,
                staticIcon = R.drawable.ic_weather_cloudynight
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_night,
                staticIcon = R.drawable.ic_weather_night
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_shower,
                staticIcon = R.drawable.ic_weather_partly_shower
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_mist,
                staticIcon = R.drawable.ic_weather_mist
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )

            in 200..299 -> WeatherIcons(
                animatedIcon = R.raw.weather_stormshowersday,
                staticIcon = R.drawable.ic_weather_stormshowersday
            )

            in 200..299 -> WeatherIcons(
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
