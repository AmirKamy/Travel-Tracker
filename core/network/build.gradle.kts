plugins {
    alias(libs.plugins.linker.android.library)
    alias(libs.plugins.linker.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.linker.core.network"
}

dependencies {

    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)

    implementation(libs.logging.interceptor)

}