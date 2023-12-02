package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.domain.model.WeatherIcons

interface IconsMapper {

    fun map(source: String): WeatherIcons
}