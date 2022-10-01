plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")
}

version = "1.0"

dependencies {
    commonMainImplementation(project(":mpp-library:base"))
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:feature"))
    commonMainImplementation(project(":mpp-library:feature:auth"))

    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}