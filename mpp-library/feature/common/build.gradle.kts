plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")

    kotlin("plugin.serialization")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = "1.0"

dependencies {
    commonMainImplementation(project(":mpp-library:base"))
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:feature"))

    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
    commonMainImplementation(libs.coroutines)

    commonMainImplementation("com.arkivanov.decompose:decompose:0.8.0")
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}