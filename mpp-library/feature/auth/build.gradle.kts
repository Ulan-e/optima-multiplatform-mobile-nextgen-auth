
plugins {
	id("multiplatform-library-convention")

	kotlin("plugin.serialization")
	id("kotlinx-serialization")
	id("kotlin-parcelize")
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
	commonMainImplementation(libs.ktorClientLogging)
	commonMainImplementation(libs.ktorClientSerialization)

	commonMainApi(libs.mokoMvvmLiveData)
	commonMainApi(libs.mokoResources)
	commonMainApi(libs.mokoErrors)
}
