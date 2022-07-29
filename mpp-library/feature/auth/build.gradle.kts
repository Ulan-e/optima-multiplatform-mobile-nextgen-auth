plugins {
    id("multiplatform-library-convention")

    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation(project(":mpp-library:core"))
    commonMainImplementation(project(":mpp-library:core:data:network"))
    commonMainImplementation(project(":mpp-library:core:data:storage"))
    commonMainImplementation(project(":mpp-library:base"))

    commonMainImplementation(libs.coroutines)
    commonMainImplementation("io.insert-koin:koin-core:3.1.4")
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)

    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoErrors)

    val kryptoVersion = "2.2.0"
    commonMainImplementation("com.soywiz.korlibs.krypto:krypto:$kryptoVersion")
}
