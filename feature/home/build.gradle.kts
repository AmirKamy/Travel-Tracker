plugins {
    alias(libs.plugins.linker.android.feature)
    alias(libs.plugins.linker.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.linker.feature.home"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.mpandroidchart)
    implementation(libs.androidx.compose.material3)
//    implementation(libs.accompanist)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    // OSMDroid
    implementation("org.osmdroid:osmdroid-android:6.1.20")
// Location
    implementation("com.google.android.gms:play-services-location:21.3.0")
// CSV (ساده)
    implementation("com.opencsv:opencsv:5.9")

    //test
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // فقط اگر تست exportCsv نیاز به FileProvider داشته باشه:
    testImplementation("org.robolectric:robolectric:4.12.1")
}