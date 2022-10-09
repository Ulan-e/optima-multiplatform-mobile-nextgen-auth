plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")

    id("com.android.library")

    kotlin("plugin.serialization")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = "1.0"

dependencies {
    commonMainImplementation(project(":mpp-library:base"))
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))
    commonMainImplementation(project(":mpp-library:feature"))
    commonMainImplementation(project(":mpp-library:feature:common"))

    commonMainImplementation(libs.coroutines)
    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)

    commonMainImplementation("com.arkivanov.decompose:decompose:0.8.0")

    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoErrors)

    commonMainApi("com.soywiz.korlibs.krypto:krypto:2.7.0")
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 19
        targetSdk = 32
    }
}