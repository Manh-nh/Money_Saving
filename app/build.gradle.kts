plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.moneymanagement"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moneymanagement"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }

    androidResources {
        generateLocaleConfig = false
    }
}

dependencies {

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.google.code.gson:gson:2.13.2")

    implementation("androidx.room:room-runtime:2.8.1")
    ksp("androidx.room:room-compiler:2.8.1")
    implementation("androidx.room:room-ktx:2.8.1")

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation(libs.androidx.core.ktx)

    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation("io.github.ShawnLin013:number-picker:2.4.13")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation ("com.tbuonomo:dotsindicator:4.3")

    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}