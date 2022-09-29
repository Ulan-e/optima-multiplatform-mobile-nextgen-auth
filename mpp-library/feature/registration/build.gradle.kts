plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "registration"
        }
    }
}

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