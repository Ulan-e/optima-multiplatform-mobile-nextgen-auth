plugins {
    kotlin("multiplatform")
    id("multiplatform-library-convention")
    id("detekt-convention")

    id("com.android.library")
    kotlin("plugin.serialization")

    id("dev.icerock.mobile.multiplatform.ios-framework")
    id("dev.icerock.mobile.multiplatform.cocoapods")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}

dependencies {
    commonMainImplementation(project(":mpp-library:base"))
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))
    commonMainImplementation(project(":mpp-library:feature"))

    commonMainImplementation(libs.coroutines)
    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)

    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoErrors)

    commonMainApi("com.soywiz.korlibs.krypto:krypto:2.7.0")
}

framework {
    export(libs.multiplatformSettings)
}

cocoaPods {
    podsProject = file("../ios-app/Pods/Pods.xcodeproj")

    pod("MCRCDynamicProxy", onlyLink = true)
}


android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 19
        targetSdk = 32
    }
}