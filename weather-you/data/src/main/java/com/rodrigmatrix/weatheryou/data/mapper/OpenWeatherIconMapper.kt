package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.weathericons.R
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherWeather
import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons

class OpenWeatherIconMapper : IconsMapper {

    override fun map(source: String): WeatherIcons {
        return when(source) {
            "01d" -> WeatherIcons(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )
            "01n" -> WeatherIcons(
                animatedIcon = R.raw.weather_night,
                staticIcon = R.drawable.ic_weather_night
            )
            "02d" -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_cloudy,
                staticIcon = R.drawable.ic_weather_partly_cloudy
            )
            "02n" -> WeatherIcons(
                animatedIcon = R.raw.weather_cloudynight,
                staticIcon = R.drawable.ic_weather_cloudynight
            )
            "03d", "03n", "04d", "04n" -> WeatherIcons(
                animatedIcon = R.raw.weather_windy,
                staticIcon = R.drawable.ic_weather_windy
            )
            "09d", "09n", "10d", "10n" -> WeatherIcons(
                animatedIcon = R.raw.weather_partly_shower,
                staticIcon = R.drawable.ic_weather_partly_shower
            )
            "11d" -> WeatherIcons(
                animatedIcon = R.raw.weather_stormshowersday,
                staticIcon = R.drawable.ic_weather_stormshowersday
            )
            "11n" -> WeatherIcons(
                animatedIcon = R.raw.weather_storm,
                staticIcon = R.drawable.ic_weather_storm
            )
            "13d" -> WeatherIcons(
                animatedIcon = R.raw.weather_snow,
                staticIcon = R.drawable.ic_weather_snow
            )
            "13n" -> WeatherIcons(
                animatedIcon = R.raw.weather_snownight,
                staticIcon = R.drawable.ic_weather_snownight
            )
            "50d", "50n" -> WeatherIcons(
                animatedIcon = R.raw.weather_mist,
                staticIcon = R.drawable.ic_weather_mist
            )
            else -> WeatherIcons(
                animatedIcon = R.raw.weather_sunny,
                staticIcon = R.drawable.ic_weather_sunny
            )
        }
    }
}
