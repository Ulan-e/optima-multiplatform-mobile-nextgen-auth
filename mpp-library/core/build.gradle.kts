plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")

    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

version = "1.0"

kotlin {
    targets.apply {
        withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
            binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
                isStatic = false
            }
        }
    }
}

dependencies {
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation("com.arkivanov.decompose:decompose:0.8.0")
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