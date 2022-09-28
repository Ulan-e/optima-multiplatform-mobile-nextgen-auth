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
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mpp-library:base"))
                implementation(project(":mpp-library:core"))
                implementation(project(":mpp-library:core:data:network"))
                implementation(project(":mpp-library:core:data:storage"))
                implementation(project(":mpp-library:feature"))

                implementation("io.insert-koin:koin-core:3.1.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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

dependencies {
    commonMainImplementation(project(":mpp-library:base"))
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))

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