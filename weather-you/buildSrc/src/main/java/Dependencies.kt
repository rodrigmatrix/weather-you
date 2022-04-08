object Dependencies {

    // Android
    const val androidxCore = "androidx.core:core-ktx:1.7.0"
    const val appCompat = "androidx.appcompat:appcompat:1.3.1"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
    const val androidxWindow = "androidx.window:window:1.0.0"
    const val splashScreen = "androidx.core:core-splashscreen:1.0.0-beta02"

    // Kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1"
    const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesVersion}"

    // Compose
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeVersion}"
    const val composeMaterial3 = "androidx.compose.material3:material3:1.0.0-alpha07"
    const val composeMaterial2 = "androidx.compose.material:material:1.2.0-alpha04"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.composeVersion}"
    const val composeNavigation = "androidx.navigation:navigation-compose:2.4.1"
    const val accompanistNavigation = "com.google.accompanist:accompanist-navigation-animation:0.16.1"
    const val accompanistFlowLayout = "com.google.accompanist:accompanist-flowlayout:0.24.0-alpha"
    const val accompanistSwipeRefresh = "com.google.accompanist:accompanist-swiperefresh:0.17.0"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:0.24.0-alpha"
    const val composeConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"

    // Material
    const val material3 = "com.google.android.material:material:1.6.0-alpha03"

    // Glance
    const val glanceAppWidget = "androidx.glance:glance-appwidget:1.0.0-alpha03"

    // Koin
    const val koinAndroid = "io.insert-koin:koin-android:3.1.5"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:3.1.5"

    // Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:29.1.0"
    const val firebaseRemoteConfig = "com.google.firebase:firebase-config-ktx"
    const val firebaseMessaging= "com.google.firebase:firebase-messaging-ktx"
    const val firebaseInAppMessaging = "com.google.firebase:firebase-inappmessaging-display"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val okHttp = "com.squareup.okhttp3:okhttp:4.9.3"
    const val retrofitKotlinx = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.3"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:2.4.2"
    const val roomCompiler = "androidx.room:room-compiler:2.4.2"
    const val roomKtx = "androidx.room:room-ktx:2.4.2"

    const val jodaTime = "joda-time:joda-time:2.10.13"

    const val gson = "com.google.code.gson:gson:2.9.0"

    const val coil = "io.coil-kt:coil-compose:2.0.0-rc01"

    const val playServicesLocation = "com.google.android.gms:play-services-location:19.0.1"

    const val lottieCompose = "com.airbnb.android:lottie-compose:4.2.0"

    // Test dependencies
    const val junit = "junit:junit:4.13.2"
    const val testExtJunit = "androidx.test.ext:junit:1.1.3"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    const val composeUiTestJunit = "androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
    const val composeTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.composeVersion}"
}