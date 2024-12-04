package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

class OpenWeatherConditionMapper {

    fun map(source: String?): WeatherCondition {
        return when(source) {
            "01d" -> WeatherCondition.Clear
            "01n" -> WeatherCondition.Clear
            "02d" -> WeatherCondition.PartlyCloudy
            "02n" -> WeatherCondition.PartlyCloudy
            "03d", "03n", "04d", "04n" -> WeatherCondition.Windy
            "09d", "09n", "10d", "10n" -> WeatherCondition.Rain
            "11d" -> WeatherCondition.ScatteredThunderstorms
            "11n" -> WeatherCondition.ScatteredThunderstorms
            "13d" -> WeatherCondition.Snow
            "13n" -> WeatherCondition.Snow
            "50d", "50n" -> WeatherCondition.Fog
            else -> WeatherCondition.Clear
        }
    }
}
