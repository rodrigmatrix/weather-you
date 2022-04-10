package com.rodrigmatrix.weatheryou.data.model.visualcrossing

import com.rodrigmatrix.weatheryou.data.R

enum class Cities(
    val displayName: Int,
    val placeId: String,
    val image: String
) {

    NEW_YORK(R.string.new_york, "ChIJOwg_06VPwokRYv534QaPC8g", "https://user-images.githubusercontent.com/7853887/162581156-61764380-3cf5-4595-9bff-a6b0e9925c04.jpeg"),
    LA(R.string.los_angeles, "ChIJE9on3F3HwoAR9AhGJW_fL-I", "https://user-images.githubusercontent.com/7853887/162581152-db0fe75e-1cfc-4032-8e3c-863378d5ad0f.jpeg"),
    TORONTO(R.string.toronto, "ChIJpTvG15DL1IkRd8S0KlBVNTI", "https://user-images.githubusercontent.com/7853887/162581170-ee7aae3f-ad15-4314-8484-0188325a7cb7.jpeg"),
    VANCOUVER(R.string.vancouver, "ChIJs0-pQ_FzhlQRi_OBm-qWkbs", "https://user-images.githubusercontent.com/7853887/162581172-97d53f3c-01dc-4847-aa31-89afa7f7b466.jpeg"),
    RIO_DE_JANEIRO(R.string.rio_de_janeiro, "ChIJW6AIkVXemwARTtIvZ2xC3FA", "https://user-images.githubusercontent.com/7853887/162581158-4b77e14e-b1ed-4291-979f-364c7bcba04b.jpeg"),
    PARIS(R.string.paris, "ChIJD7fiBh9u5kcRYJSMaMOCCwQ", "https://user-images.githubusercontent.com/7853887/162581396-8b213cf0-5915-4c97-adba-06d8d9d2b54b.png"),
    LONDON(R.string.london, "ChIJdd4hrwug2EcRmSrV3Vo6llI", "https://user-images.githubusercontent.com/7853887/162581150-bd0db69f-a193-44de-91b0-51b1cd4d6afb.jpeg"),
    HONG_KONG(R.string.hong_kong, "ChIJD5gyo-3iAzQRfMnq27qzivA", "https://user-images.githubusercontent.com/7853887/162581316-48ef9316-2b86-4964-b2fc-34fed2b2b5cb.png"),
    TOKYO(R.string.tokyo, "ChIJXSModoWLGGARILWiCfeu2M0", "https://user-images.githubusercontent.com/7853887/162581166-b48a84bd-cf16-4304-9563-d9a40aa257f6.png"),
    SEOUL(R.string.seoul, "ChIJzWXFYYuifDUR64Pq5LTtioU", "https://user-images.githubusercontent.com/7853887/162581162-669582fc-0fa1-497e-a6b4-c66912566fb1.jpeg"),
    NEW_DELHI(R.string.new_delhi, "ChIJLbZ-NFv9DDkRzk0gTkm3wlI", "https://user-images.githubusercontent.com/7853887/162581290-bc3e7c48-f478-4f69-9c7e-20e06307d0ed.png"),
    SYDNEY(R.string.sydney, "ChIJP3Sa8ziYEmsRUKgyFmh9AQM", "https://user-images.githubusercontent.com/7853887/162581509-22394641-6b0a-49f7-8155-db77b72aa872.jpeg")
}