
import dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature

plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("kotlinx-serialization")
    id("kotlin-parcelize")

    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.mobile.multiplatform.ios-framework")
    id("dev.icerock.mobile.multiplatform-network-generator")
    id("dev.icerock.mobile.multiplatform.cocoapods")
    id("dev.icerock.moko.kswift")
}

dependencies {
    commonMainApi(project(":mpp-library:base"))
    commonMainApi(project(":mpp-library:core"))
    commonMainApi(project(":mpp-library:core:data:network"))
    commonMainApi(project(":mpp-library:core:data:storage"))
    commonMainApi(projects.mppLibrary.feature)
    commonMainApi(projects.mppLibrary.feature.auth)
    commonMainApi(project(":mpp-library:feature:common"))
    commonMainApi(project(":mpp-library:feature:main"))
    commonMainApi(project(":mpp-library:feature:payments"))
    commonMainApi(project(":mpp-library:feature:registration"))
    commonMainApi(project(":mpp-library:feature:transfers"))

    commonMainImplementation("io.insert-koin:koin-core:3.1.4")

    commonMainImplementation(libs.coroutines)

    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClientLogging)

    androidMainImplementation(libs.multidex)
    androidMainImplementation(libs.lifecycleViewModel)

    commonMainApi(libs.multiplatformSettings)
    commonMainApi(libs.napier)
    commonMainApi(libs.mokoParcelize)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoMvvmCore)
    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoMvvmState)
    commonMainApi(libs.mokoUnits)
    commonMainApi(libs.mokoFields)
    commonMainApi(libs.mokoErrors)
    commonMainImplementation(libs.mokoNetwork)
    commonMainImplementation(libs.mokoNetworkErrors)
    commonMainApi(libs.mokoCrashReportingCore)
    commonMainApi(libs.mokoCrashReportingCrashlytics)
    commonMainApi(libs.mokoCrashReportingNapier)

    commonMainImplementation("com.arkivanov.decompose:decompose:1.0.0-alpha-04")


    commonTestImplementation(libs.mokoTestCore)
    commonTestImplementation(libs.mokoMvvmTest)
    commonTestImplementation(libs.mokoUnitsTest)
    commonTestImplementation(libs.multiplatformSettingsTest)
    commonTestImplementation(libs.ktorClientMock)

    val composeVersion = "1.3.0-alpha01"
    androidMainImplementation("androidx.compose.animation:animation:$composeVersion")
    androidMainImplementation("androidx.compose.foundation:foundation:$composeVersion")
    androidMainImplementation("androidx.compose.material:material:$composeVersion")
    androidMainImplementation("androidx.compose.runtime:runtime:$composeVersion")
    androidMainImplementation("androidx.compose.ui:ui:$composeVersion")
}

multiplatformResources {
    multiplatformResourcesPackage = "kg.optima.mobile"
}

val modules = listOf(
    projects.mppLibrary.base,
    projects.mppLibrary.core,
//    projects.mppLibrary.feature.auth,
)

framework {
    modules.forEach { export(it) }

    export(libs.multiplatformSettings)
    export(libs.napier)
    export(libs.mokoParcelize)
    export(libs.mokoResources)
    export(libs.mokoGraphics)
    export(libs.mokoMvvmCore)
    export(libs.mokoMvvmLiveData)
    export(libs.mokoMvvmState)
    export(libs.mokoUnits)
    export(libs.mokoFields)
    export(libs.mokoErrors)
    export(libs.mokoCrashReportingCore)
    export(libs.mokoCrashReportingCrashlytics)
    export(libs.mokoCrashReportingNapier)
}

cocoaPods {
    podsProject = file("../ios-app/Pods/Pods.xcodeproj")

    pod("MCRCDynamicProxy", onlyLink = true)
}

mokoNetwork {
    spec("serverApi") {
        inputSpec = file("src/api/openapi.yml")
    }
}

kswift {
    install(SealedToSwiftEnumFeature)
}
