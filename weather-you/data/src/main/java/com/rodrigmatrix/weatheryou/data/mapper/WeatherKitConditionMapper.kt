package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

class WeatherKitConditionMapper {

    fun map(source: String?): WeatherCondition {
        return try {
            WeatherCondition.valueOf(source.orEmpty())
        } catch (e: Exception) {
            WeatherCondition.Clear
        }
    }
}