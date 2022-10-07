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
            baseName = "network"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mpp-library:core"))

                implementation("io.insert-koin:koin-core:3.1.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

                implementation(libs.ktorClient)
                implementation(libs.ktorClientLogging)
                implementation(libs.ktorClientSerialization)

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("io.ktor:ktor-client-okhttp:1.6.8")
            }
        }
        val androidTest by getting
        val iosX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:1.6.8")
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-ios:1.6.8")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}
dependencies {
    implementation(project(mapOf("path" to ":mpp-library:core")))
}
