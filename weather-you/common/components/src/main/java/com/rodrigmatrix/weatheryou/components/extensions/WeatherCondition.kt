package com.rodrigmatrix.weatheryou.components.extensions

import com.rodrigmatrix.weatheryou.weathericons.R
import com.rodrigmatrix.weatheryou.components.R as StringsR
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

fun WeatherCondition.getAnimatedIcon(): Int = when (this) {
    WeatherCondition.SUNNY_DAY -> R.raw.weather_sunny
    WeatherCondition.CLEAR_NIGHT -> R.raw.weather_night
    WeatherCondition.PARTLY_CLOUDY_DAY -> R.raw.weather_partly_cloudy
    WeatherCondition.PARTLY_CLOUDY_NIGHT -> R.raw.weather_cloudynight
    WeatherCondition.CLOUDY -> R.raw.weather_partly_cloudy
    WeatherCondition.WINDY -> R.raw.weather_windy
    WeatherCondition.SNOW_DAY -> R.raw.weather_snow_sunny
    WeatherCondition.SNOW_NIGHT -> R.raw.weather_snownight
    WeatherCondition.MIST -> R.raw.weather_mist
    WeatherCondition.STORM_RAIN_DAY -> R.raw.weather_snownight
    WeatherCondition.STORM_RAIN_NIGHT -> R.raw.weather_stormshowersday
    WeatherCondition.RAIN_DAY -> R.raw.weather_partly_shower
    WeatherCondition.RAIN_NIGHT -> R.raw.weather_rainynight
}

fun WeatherCondition.getStaticIcon(): Int = when (this) {
    WeatherCondition.SUNNY_DAY -> R.drawable.ic_weather_sunny
    WeatherCondition.CLEAR_NIGHT -> R.drawable.ic_weather_night
    WeatherCondition.PARTLY_CLOUDY_DAY -> R.drawable.ic_weather_partly_cloudy
    WeatherCondition.PARTLY_CLOUDY_NIGHT -> R.drawable.ic_weather_cloudynight
    WeatherCondition.CLOUDY -> R.drawable.ic_weather_windy
    WeatherCondition.WINDY -> R.drawable.ic_weather_windy
    WeatherCondition.SNOW_DAY -> R.drawable.ic_weather_snow_sunny
    WeatherCondition.SNOW_NIGHT -> R.drawable.ic_weather_snownight
    WeatherCondition.MIST -> R.drawable.ic_weather_mist
    WeatherCondition.STORM_RAIN_DAY -> R.drawable.ic_weather_stormshowersday
    WeatherCondition.STORM_RAIN_NIGHT -> R.drawable.ic_weather_storm
    WeatherCondition.RAIN_DAY -> R.drawable.ic_weather_partly_shower
    WeatherCondition.RAIN_NIGHT -> R.drawable.ic_weather_rainynight
}

fun WeatherCondition.getString(): Int = when (this) {
    WeatherCondition.SUNNY_DAY -> StringsR.string.condition_sunny
    WeatherCondition.CLEAR_NIGHT -> StringsR.string.condition_clear
    WeatherCondition.PARTLY_CLOUDY_DAY -> StringsR.string.condition_partly_cloudy
    WeatherCondition.PARTLY_CLOUDY_NIGHT -> StringsR.string.condition_partly_cloudy
    WeatherCondition.CLOUDY -> StringsR.string.condition_cloudy
    WeatherCondition.WINDY -> StringsR.string.condition_windy
    WeatherCondition.SNOW_DAY -> StringsR.string.condition_snow
    WeatherCondition.SNOW_NIGHT -> StringsR.string.condition_snow
    WeatherCondition.MIST -> StringsR.string.condition_mist
    WeatherCondition.STORM_RAIN_DAY -> StringsR.string.condition_thunderstorm
    WeatherCondition.STORM_RAIN_NIGHT -> StringsR.string.condition_thunderstorm
    WeatherCondition.RAIN_DAY -> StringsR.string.condition_rain
    WeatherCondition.RAIN_NIGHT -> StringsR.string.condition_rain
}
    