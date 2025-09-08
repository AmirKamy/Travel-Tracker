plugins {
    alias(libs.plugins.linker.android.library)
    alias(libs.plugins.linker.android.room)
    alias(libs.plugins.linker.android.hilt)
}

android {
    namespace = "com.linker.core.database"
}

dependencies {

    api(projects.core.model)


    api(libs.androidx.paging.runtime)
    api(libs.androidx.paging.compose)
    api(libs.androidx.room.paging)
}