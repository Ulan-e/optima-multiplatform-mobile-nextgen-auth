plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")

    id("dev.icerock.mobile.multiplatform.cocoapods")
    id("dev.icerock.mobile.multiplatform.ios-framework")

    kotlin("plugin.serialization")
}

version = "1.0"

dependencies {
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))

    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    commonMainImplementation("io.insert-koin:koin-core:3.1.4")

    commonMainImplementation("co.touchlab:stately-concurrency:1.2.0")

    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClientLogging)
    commonMainImplementation(libs.ktorClientSerialization)

    iosMainImplementation("io.ktor:ktor-client-ios:1.6.7")
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}
