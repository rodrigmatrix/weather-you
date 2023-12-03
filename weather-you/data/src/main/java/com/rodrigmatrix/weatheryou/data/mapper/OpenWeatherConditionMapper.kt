package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

class OpenWeatherConditionMapper {

    fun map(source: String?): WeatherCondition {
        return when(source) {
            "01d" -> WeatherCondition.SUNNY_DAY
            "01n" -> WeatherCondition.CLEAR_NIGHT
            "02d" -> WeatherCondition.PARTLY_CLOUDY_DAY
            "02n" -> WeatherCondition.PARTLY_CLOUDY_NIGHT
            "03d", "03n", "04d", "04n" -> WeatherCondition.WINDY
            "09d", "09n", "10d", "10n" -> WeatherCondition.RAIN_DAY
            "11d" -> WeatherCondition.STORM_RAIN_DAY
            "11n" -> WeatherCondition.STORM_RAIN_NIGHT
            "13d" -> WeatherCondition.SNOW_DAY
            "13n" -> WeatherCondition.SNOW_NIGHT
            "50d", "50n" -> WeatherCondition.MIST
            else -> WeatherCondition.SUNNY_DAY
        }
    }
}
