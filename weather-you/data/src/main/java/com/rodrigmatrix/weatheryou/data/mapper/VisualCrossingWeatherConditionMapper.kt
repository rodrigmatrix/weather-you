package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

class VisualCrossingWeatherConditionMapper {

    fun map(source: String?): WeatherCondition {
        return when(source) {
            "partly-cloudy-day" -> WeatherCondition.PARTLY_CLOUDY_DAY

            "partly-cloudy-night" -> WeatherCondition.PARTLY_CLOUDY_NIGHT

            "cloudy" -> WeatherCondition.CLOUDY

            "wind" -> WeatherCondition.WINDY

            "clear-day"-> WeatherCondition.CLEAR_NIGHT

            "clear-night" -> WeatherCondition.CLEAR_NIGHT

            "snow" -> WeatherCondition.SNOW_DAY

            "snow-night" -> WeatherCondition.SNOW_NIGHT

            "rain" -> WeatherCondition.RAIN_DAY

            "fog" -> WeatherCondition.MIST

            "snow-showers-day" -> WeatherCondition.RAIN_DAY

            "snow-showers-night" -> WeatherCondition.RAIN_NIGHT

            "thunder-rain" -> WeatherCondition.STORM_RAIN_DAY

            "thunder-showers-day" -> WeatherCondition.STORM_RAIN_NIGHT

            "thunder-showers-night" -> WeatherCondition.STORM_RAIN_NIGHT

            else -> WeatherCondition.SUNNY_DAY
        }
    }
}