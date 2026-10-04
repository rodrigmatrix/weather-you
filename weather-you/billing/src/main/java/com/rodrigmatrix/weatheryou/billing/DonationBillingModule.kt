package com.rodrigmatrix.weatheryou.billing

import org.koin.dsl.module

val donationBillingModule = module {
    single { DonationBillingManager(get(), get()) }
}
