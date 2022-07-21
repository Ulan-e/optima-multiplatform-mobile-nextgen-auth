plugins {
    id("multiplatform-library-convention")

    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))
    commonMainImplementation(project(":mpp-library:base"))

    commonMainImplementation(libs.coroutines)
    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    commonMainImplementation("io.ktor:ktor-client-core:2.0.3")

    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoErrors)
}
