package com.rodrigmatrix.weatheryou.components.extensions

import com.rodrigmatrix.weatheryou.weathericons.R
import com.rodrigmatrix.weatheryou.domain.R as StringsR
import com.rodrigmatrix.weatheryou.domain.model.WeatherCondition

fun WeatherCondition.getAnimatedIcon(isDaylight: Boolean): Int = when (this) {
    WeatherCondition.Clear,
    WeatherCondition.Frigid,
    WeatherCondition.Hot -> if (isDaylight) {
        R.raw.weather_sunny
    } else {
        R.raw.weather_night
    }

    WeatherCondition.PartlyCloudy, WeatherCondition.MostlyClear -> if (isDaylight) {
        R.raw.weather_partly_cloudy
    } else {
        R.raw.weather_cloudynight
    }

    WeatherCondition.Cloudy,
    WeatherCondition.Hurricane,
    WeatherCondition.MostlyCloudy -> R.raw.weather_windy

    WeatherCondition.Fog,
    WeatherCondition.Haze,
    WeatherCondition.Dust,
    WeatherCondition.Smoke,
    WeatherCondition.Breezy,
    WeatherCondition.Tornado,
    WeatherCondition.TropicalStorm,
    WeatherCondition.Windy -> R.raw.weather_mist

    WeatherCondition.Rain,
    WeatherCondition.HeavyRain,
    WeatherCondition.Hail,
    WeatherCondition.Drizzle,
    WeatherCondition.Sleet,
    WeatherCondition.FreezingDrizzle,
    WeatherCondition.FreezingRain,
    WeatherCondition.Showers,
    WeatherCondition.MixedRainAndSleet,
    WeatherCondition.ScatteredShowers,
    WeatherCondition.MixedRainfall -> if (isDaylight) {
        R.raw.weather_partly_shower
    } else {
        R.raw.weather_rainynight
    }

    WeatherCondition.Snow,
    WeatherCondition.HeavySnow,
    WeatherCondition.BlowingSnow,
    WeatherCondition.Flurries,
    WeatherCondition.Blizzard,
    WeatherCondition.ScatteredSnowShowers,
    WeatherCondition.SnowShowers,
    WeatherCondition.MixedSnowAndSleet,
    WeatherCondition.MixedRainAndSnow -> if (isDaylight) {
        R.raw.weather_snow_sunny
    } else {
        R.raw.weather_snownight
    }

    WeatherCondition.Thunderstorm,
    WeatherCondition.Thunderstorms,
    WeatherCondition.IsolatedThunderstorms,
    WeatherCondition.SevereThunderstorm,
    WeatherCondition.ScatteredThunderstorms -> if (isDaylight) {
        R.raw.weather_stormshowersday
    } else {
        R.raw.weather_storm
    }
}

fun WeatherCondition.getStaticIcon(isDaylight: Boolean): Int = when (this) {
    WeatherCondition.Clear,
    WeatherCondition.Frigid,
    WeatherCondition.Hot -> if (isDaylight) {
        R.drawable.ic_weather_sunny
    } else {
        R.drawable.ic_weather_night
    }

    WeatherCondition.PartlyCloudy, WeatherCondition.MostlyClear -> if (isDaylight) {
        R.drawable.ic_weather_partly_cloudy
    } else {
        R.drawable.ic_weather_cloudynight
    }

    WeatherCondition.Cloudy,
    WeatherCondition.Hurricane,
    WeatherCondition.MostlyCloudy -> R.drawable.ic_weather_windy

    WeatherCondition.Fog,
    WeatherCondition.Haze,
    WeatherCondition.Dust,
    WeatherCondition.Smoke,
    WeatherCondition.Breezy,
    WeatherCondition.Tornado,
    WeatherCondition.TropicalStorm,
    WeatherCondition.Windy -> R.drawable.ic_weather_mist

    WeatherCondition.Rain,
    WeatherCondition.HeavyRain,
    WeatherCondition.Hail,
    WeatherCondition.Drizzle,
    WeatherCondition.Sleet,
    WeatherCondition.FreezingDrizzle,
    WeatherCondition.FreezingRain,
    WeatherCondition.Showers,
    WeatherCondition.MixedRainAndSleet,
    WeatherCondition.ScatteredShowers,
    WeatherCondition.MixedRainfall -> if (isDaylight) {
        R.drawable.ic_weather_partly_shower
    } else {
        R.drawable.ic_weather_rainynight
    }

    WeatherCondition.Snow,
    WeatherCondition.HeavySnow,
    WeatherCondition.BlowingSnow,
    WeatherCondition.Flurries,
    WeatherCondition.Blizzard,
    WeatherCondition.ScatteredSnowShowers,
    WeatherCondition.SnowShowers,
    WeatherCondition.MixedSnowAndSleet,
    WeatherCondition.MixedRainAndSnow -> if (isDaylight) {
        R.drawable.ic_weather_snow_sunny
    } else {
        R.drawable.ic_weather_snownight
    }

    WeatherCondition.Thunderstorm,
    WeatherCondition.IsolatedThunderstorms,
    WeatherCondition.SevereThunderstorm,
    WeatherCondition.Thunderstorms,
    WeatherCondition.ScatteredThunderstorms -> if (isDaylight) {
        R.drawable.ic_weather_stormshowersday
    } else {
        R.drawable.ic_weather_storm
    }
}

fun WeatherCondition.getString(isDaylight: Boolean): Int = when (this) {
    WeatherCondition.Clear -> StringsR.string.condition_clear

    WeatherCondition.Frigid,
    WeatherCondition.Hot -> if (isDaylight) {
        StringsR.string.condition_sunny
    } else {
        StringsR.string.condition_clear
    }

    WeatherCondition.MostlyClear -> StringsR.string.condition_mostly_clear

    WeatherCondition.PartlyCloudy -> StringsR.string.condition_partly_cloudy

    WeatherCondition.MostlyCloudy -> StringsR.string.condition_mostly_cloudy

    WeatherCondition.Cloudy,
    WeatherCondition.Hurricane -> StringsR.string.condition_cloudy

    WeatherCondition.Fog -> StringsR.string.condition_fog

    WeatherCondition.Haze -> StringsR.string.condition_haze

    WeatherCondition.Dust,
    WeatherCondition.Smoke,
    WeatherCondition.Breezy,
    WeatherCondition.Tornado,
    WeatherCondition.TropicalStorm,
    WeatherCondition.Windy -> StringsR.string.condition_windy

    WeatherCondition.HeavyRain -> StringsR.string.condition_heavy_rain

    WeatherCondition.Hail -> StringsR.string.condition_hail

    WeatherCondition.Rain,
    WeatherCondition.Drizzle,
    WeatherCondition.Sleet,
    WeatherCondition.FreezingDrizzle,
    WeatherCondition.FreezingRain,
    WeatherCondition.Showers,
    WeatherCondition.MixedRainAndSleet,
    WeatherCondition.ScatteredShowers,
    WeatherCondition.MixedRainfall -> StringsR.string.condition_rain

    WeatherCondition.Snow,
    WeatherCondition.HeavySnow,
    WeatherCondition.BlowingSnow,
    WeatherCondition.Flurries,
    WeatherCondition.Blizzard,
    WeatherCondition.ScatteredSnowShowers,
    WeatherCondition.SnowShowers,
    WeatherCondition.MixedSnowAndSleet,
    WeatherCondition.MixedRainAndSnow -> StringsR.string.condition_snow

    WeatherCondition.IsolatedThunderstorms -> StringsR.string.condition_isolated_thunderstorm

    WeatherCondition.Thunderstorm,
    WeatherCondition.SevereThunderstorm,
    WeatherCondition.Thunderstorms,
    WeatherCondition.ScatteredThunderstorms -> StringsR.string.condition_thunderstorm
}
    