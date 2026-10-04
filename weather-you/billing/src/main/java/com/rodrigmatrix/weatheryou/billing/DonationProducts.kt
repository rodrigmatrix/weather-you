package com.rodrigmatrix.weatheryou.billing

/** Create matching one-time, non-consumable products in the Google Play Console. */
object DonationProducts {
    const val SUPPORTER = "donation_supporter"
    const val FRIEND = "donation_friend"
    const val PATRON = "donation_patron"
    val all = listOf(SUPPORTER, FRIEND, PATRON)
}
