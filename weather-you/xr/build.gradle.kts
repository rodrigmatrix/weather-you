plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.rodrigmatrix.weatheryou.xr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rodrigmatrix.weatheryou"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(LocalModules.domain))
    implementation(project(LocalModules.data))
    implementation(project(LocalModules.core))
    implementation(project(LocalModules.ads))
    implementation(project(LocalModules.components))
    implementation(project(LocalModules.home))
    implementation(project(LocalModules.addLocation))
    implementation(project(LocalModules.weatherIcons))
    implementation(project(LocalModules.about))
    implementation(project(LocalModules.settings))
    implementation(project(LocalModules.locationDetails))
    implementation(project(LocalModules.widgets))
    implementation(libs.androidx.splash.screen)


    implementation(libs.xr.compose)
    implementation(libs.xr.compose.material3)
    implementation(libs.xr.runtime)
    implementation(libs.xr.scenecore)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.window)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.serialization)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.adaptive.layout)
    implementation(libs.androidx.adaptive.navigation)
    implementation(libs.androidx.adaptive.navigation.suite)
    implementation(libs.compose.constraint.layout)
    implementation(libs.compose.window.size)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation)
    implementation(libs.accompanist.adaptive)

    implementation(libs.glanceAppWidget)
    implementation(libs.glanceAppWidget.material3)
    implementation(libs.glance.preview)
    implementation(libs.glanceAppWidget.preview)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.workManager)

    implementation(libs.google.play.location)
    implementation(libs.google.play.review)
    implementation(libs.google.play.ads)
    implementation(libs.google.play.billing)

    implementation(libs.jodaTime)

    implementation(libs.gson)

    implementation(libs.coil)

    implementation(libs.lottie.compose)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeTestManifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.composeUiTestJunit)
}