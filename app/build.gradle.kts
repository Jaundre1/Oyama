plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.oyama"
    compileSdk = 34

    buildFeatures {
        buildConfig = true // Enable BuildConfig fields
    }

    defaultConfig {
        applicationId = "com.example.oyama"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add AWS_API_KEY to build config
        buildConfigField("String", "AWS_API_KEY", "\"your_api_key_here\"") // Replace with your actual API key
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // CameraX dependencies
    val camerax_version = "1.1.0"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:1.0.0-alpha31")

    // ML Kit Barcode Scanning dependency
    implementation("com.google.mlkit:barcode-scanning:16.1.1")

    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp Logging Interceptor dependency
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // ViewBinding
    implementation("androidx.databinding:viewbinding:7.0.4")

    // Spinner dropdown functionality
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
