
enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("dev.icerock.gradle.talaiot") version("3.+")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

includeBuild("build-logic")

include(":android-app")
include(":mpp-library")
include(":mpp-library:base")
include(":mpp-library:core")
include(":mpp-library:core:data:network")
include(":mpp-library:core:data:storage")
include(":mpp-library:core:presentation:design-system:android")
//include(":mpp-library:core:presentation:design-system:ios")
include(":mpp-library:feature")
include(":mpp-library:feature:auth")
include(":mpp-library:feature:main")
include(":mpp-library:feature:payments")
include(":mpp-library:feature:transfers")
include(":mpp-library:feature:registration")
include(":mpp-library:feature:common")
