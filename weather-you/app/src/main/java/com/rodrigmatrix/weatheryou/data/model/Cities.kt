package com.rodrigmatrix.weatheryou.data.model

import com.rodrigmatrix.weatheryou.R

enum class Cities(
    val displayName: Int,
    val fullName: String,
    val image: String
) {

    NEW_YORK(R.string.new_york, "New York, NY", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Fnew_york.jpeg?alt=media&token=a31070c6-21ba-49a0-9b62-631570759ad0"),
    LA(R.string.los_angeles, "Los Angeles, CA, US", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Fla.jpeg?alt=media&token=9c05aa76-b502-46a6-9928-7cdf460a184d"),
    TORONTO(R.string.toronto, "Toronto, ON", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Ftoronto.jpeg?alt=media&token=ccbc632a-4144-4c9c-aa3e-0c4f5b655357"),
    VANCOUVER(R.string.vancouver, "Vancouver, BC", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Fvancouver.jpeg?alt=media&token=6eb1fb97-9d94-4648-909f-ea83d6a80ee7"),
    RIO_DE_JANEIRO(R.string.rio_de_janeiro, "Rio de Janeiro, RJ", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Frio-de-janeiro.jpeg?alt=media&token=2a141219-bddf-409e-a38b-f010f6fc27d9"),
    SAO_PAULO(R.string.sao_paulo, "São Paulo, SP", "https://firebasestorage.googleapis.com/v0/b/weatheryou-cfaf5.appspot.com/o/cities%2Fsao_paulo.jpeg?alt=media&token=49207e12-a561-4ae4-8567-e14f0d858001")
}