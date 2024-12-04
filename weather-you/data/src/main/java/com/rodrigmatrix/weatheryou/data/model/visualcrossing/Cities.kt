package com.rodrigmatrix.weatheryou.data.model.visualcrossing

import com.rodrigmatrix.weatheryou.data.R

enum class Cities(
    val displayName: Int,
    val lat: Double,
    val long: Double,
    val image: String,
    val countryCode: String,
    val timezone: String,
) {

    NEW_YORK(R.string.new_york, 40.6970193, -74.3093288, "https://user-images.githubusercontent.com/7853887/162581156-61764380-3cf5-4595-9bff-a6b0e9925c04.jpeg", "US", "America/New_York"),
    LA(R.string.los_angeles,34.052235, -118.243683, "https://user-images.githubusercontent.com/7853887/162581152-db0fe75e-1cfc-4032-8e3c-863378d5ad0f.jpeg", "US", "America/Los_Angeles"),
    TORONTO(R.string.toronto,43.651070, -79.347015, "https://user-images.githubusercontent.com/7853887/162581170-ee7aae3f-ad15-4314-8484-0188325a7cb7.jpeg", "CA", "America/Toronto"),
    VANCOUVER(R.string.vancouver,49.246292, -123.116226, "https://user-images.githubusercontent.com/7853887/162581172-97d53f3c-01dc-4847-aa31-89afa7f7b466.jpeg", "CA", "America/Vancouver"),
    RIO_DE_JANEIRO(R.string.rio_de_janeiro, -22.908333, -43.196388, "https://user-images.githubusercontent.com/7853887/162581158-4b77e14e-b1ed-4291-979f-364c7bcba04b.jpeg", "BR", "America/Sao_Paulo"),
    PARIS(R.string.paris,48.864716, 2.349014,  "https://user-images.githubusercontent.com/7853887/162581396-8b213cf0-5915-4c97-adba-06d8d9d2b54b.png", "FR", "Europe/Paris"),
    LONDON(R.string.london,51.509865, -0.118092,  "https://user-images.githubusercontent.com/7853887/162581150-bd0db69f-a193-44de-91b0-51b1cd4d6afb.jpeg", "GB", "Europe/London"),
    HONG_KONG(R.string.hong_kong,22.302711, 114.177216, "https://user-images.githubusercontent.com/7853887/162581316-48ef9316-2b86-4964-b2fc-34fed2b2b5cb.png", "HK", "Asia/Hong_Kong"),
    TOKYO(R.string.tokyo,35.652832, 139.839478,  "https://user-images.githubusercontent.com/7853887/162581166-b48a84bd-cf16-4304-9563-d9a40aa257f6.png", "JP", "Asia/Tokyo"),
    SEOUL(R.string.seoul,37.532600, 127.024612,  "https://user-images.githubusercontent.com/7853887/162581162-669582fc-0fa1-497e-a6b4-c66912566fb1.jpeg", "KR", "Asia/Seoul"),
    NEW_DELHI(R.string.new_delhi,28.644800, 77.216721, "https://user-images.githubusercontent.com/7853887/162581290-bc3e7c48-f478-4f69-9c7e-20e06307d0ed.png", "IN", "Asia/Kolkata"),
    SYDNEY(R.string.sydney,-33.865143, 151.209900, "https://user-images.githubusercontent.com/7853887/162581509-22394641-6b0a-49f7-8155-db77b72aa872.jpeg", "AU", "Australia/Sydney")
}