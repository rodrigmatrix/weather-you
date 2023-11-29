object Dependencies {

    // Android
    const val androidxCore = "androidx.core:core-ktx:1.10.1"
    const val appCompat = "androidx.appcompat:appcompat:1.6.1"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
    const val androidxWindow = "androidx.window:window:1.0.0"
    const val splashScreen = "androidx.core:core-splashscreen:1.0.1"

    // Kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1"
    const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesVersion}"

    // Compose
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeVersion}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.composeVersion}"
    const val composeMaterial3 = "androidx.compose.material3:material3:1.2.0-alpha10"
    const val composeMaterial2 = "androidx.compose.material:material:1.6.0-alpha08"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.composeVersion}"
    const val composeNavigation = "androidx.navigation:navigation-compose:2.5.3"
    const val accompanistNavigation = "com.google.accompanist:accompanist-navigation-animation:0.16.1"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:0.24.0-alpha"
    const val composeConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    const val composeWindowSizeClass = "androidx.compose.material3:material3-window-size-class:1.2.0-alpha01"
    const val accompanistAdaptive = "com.google.accompanist:accompanist-adaptive:0.31.1-alpha"

    const val composeTvFoundation = "androidx.tv:tv-foundation:${Versions.composeTvVersion}"
    const val composeTvMaterial = "androidx.tv:tv-material:${Versions.composeTvVersion}"

    // Material
    const val material3 = "com.google.android.material:material:1.9.0"

    // Glance
    const val glanceAppWidget = "androidx.glance:glance-appwidget:1.0.0-beta01"

    // Koin
    const val koinAndroid = "io.insert-koin:koin-android:3.4.2"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:3.4.5"

    // Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:32.0.0"
    const val firebaseRemoteConfig = "com.google.firebase:firebase-config-ktx"
    const val firebaseMessaging= "com.google.firebase:firebase-messaging-ktx"
    const val firebaseInAppMessaging = "com.google.firebase:firebase-inappmessaging-display"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseDatabase = "com.google.firebase:firebase-database-ktx"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val okHttp = "com.squareup.okhttp3:okhttp-bom:4.11.0"
    const val retrofitKotlinx = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:2.5.1"
    const val roomCompiler = "androidx.room:room-compiler:2.5.1"
    const val roomKtx = "androidx.room:room-ktx:2.5.1"

    const val jodaTime = "joda-time:joda-time:2.12.5"

    const val gson = "com.google.code.gson:gson:2.9.0"

    const val coil = "io.coil-kt:coil-compose:2.3.0"

    const val playServicesLocation = "com.google.android.gms:play-services-location:21.0.1"

    const val lottieCompose = "com.airbnb.android:lottie-compose:6.0.0"
}