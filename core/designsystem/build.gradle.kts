plugins {
    alias(libs.plugins.linker.android.library)
    alias(libs.plugins.linker.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.linker.core.designsystem"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.material.icon.extended)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.ui.ui)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.navigation.compose)


//    implementation(libs.coil)
}