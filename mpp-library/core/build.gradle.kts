plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")

    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = "1.0"

dependencies {
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation("com.arkivanov.decompose:decompose:1.0.0-alpha-04")
    commonMainApi("com.soywiz.korlibs.krypto:krypto:2.7.0")
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}