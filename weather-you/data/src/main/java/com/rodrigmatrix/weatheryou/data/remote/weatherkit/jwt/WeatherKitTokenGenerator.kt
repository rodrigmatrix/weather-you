package com.rodrigmatrix.weatheryou.data.remote.weatherkit.jwt

import com.rodrigmatrix.weatheryou.data.BuildConfig
import io.jsonwebtoken.Jwts
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class WeatherKitTokenGenerator {

    @OptIn(ExperimentalEncodingApi::class)
    fun generateToken(): String {
        val currentTime = System.currentTimeMillis()
        val keySpec = PKCS8EncodedKeySpec(Base64.decode(BuildConfig.WEATHER_KIT_TOKEN.toByteArray()))
        val privateKey = KeyFactory.getInstance("EC").generatePrivate(keySpec)
        return Jwts.builder()
            .header()
                .add("alg", "ES256")
                .add("id", BuildConfig.WEATHER_KIT_ID)
                .add("kid", BuildConfig.WEATHER_KIT_KID)
                .and()
            .claims()
                .add("iss", BuildConfig.WEATHER_KIT_ISS)
                .add("iat" , currentTime / 1000)
                .add("exp", (currentTime / 1000) + 3600)
                .add("sub", BuildConfig.WEATHER_KIT_SUB)
                .and()
            .signWith(privateKey, Jwts.SIG.ES256)
            .compact()
    }
}