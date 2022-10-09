plugins {
    id("multiplatform-library-convention")

    kotlin("plugin.serialization")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = "1.0"

dependencies {
    commonMainImplementation(projects.mppLibrary.base)
    commonMainImplementation(projects.mppLibrary.core)
    commonMainImplementation(projects.mppLibrary.core.data.network)
    commonMainImplementation(projects.mppLibrary.core.data.storage)
    commonMainImplementation(projects.mppLibrary.feature)
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}