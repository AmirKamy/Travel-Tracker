plugins {
    alias(libs.plugins.linker.android.library)
}

android {
    namespace = "com.linker.core.domain"

}

dependencies {
    api(projects.core.model)
    api(projects.core.common)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}