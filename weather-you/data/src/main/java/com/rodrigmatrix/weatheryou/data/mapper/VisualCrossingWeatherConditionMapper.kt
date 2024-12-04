package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

class VisualCrossingWeatherConditionMapper {

    fun map(source: String?): WeatherCondition {
        return when(source) {
            "partly-cloudy-day" -> WeatherCondition.PartlyCloudy

            "partly-cloudy-night" -> WeatherCondition.PartlyCloudy

            "cloudy" -> WeatherCondition.Cloudy

            "wind" -> WeatherCondition.Windy

            "clear-day"-> WeatherCondition.Clear

            "clear-night" -> WeatherCondition.Clear

            "snow" -> WeatherCondition.Snow

            "snow-night" -> WeatherCondition.Snow

            "rain" -> WeatherCondition.Rain

            "fog" -> WeatherCondition.Fog

            "snow-showers-day" -> WeatherCondition.Rain

            "snow-showers-night" -> WeatherCondition.Snow

            "thunder-rain" -> WeatherCondition.ScatteredThunderstorms

            "thunder-showers-day" -> WeatherCondition.ScatteredThunderstorms

            "thunder-showers-night" -> WeatherCondition.ScatteredThunderstorms

            else -> WeatherCondition.Clear
        }
    }
}