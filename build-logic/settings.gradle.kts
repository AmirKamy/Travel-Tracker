dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://google403.ir") }
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
