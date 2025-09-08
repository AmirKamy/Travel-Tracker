plugins {
    alias(libs.plugins.linker.android.application)
    alias(libs.plugins.linker.android.application.compose)
    alias(libs.plugins.linker.android.hilt)
}

android {
    namespace = "com.example.traveltracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.traveltracker"
        minSdk = 26
        targetSdk = 34
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
    }
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.register)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
// OSMDroid
    implementation("org.osmdroid:osmdroid-android:6.1.20")
// Location
    implementation("com.google.android.gms:play-services-location:21.3.0")
// CSV (ساده)
    implementation("com.opencsv:opencsv:5.9")
}