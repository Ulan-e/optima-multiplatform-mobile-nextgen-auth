/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("dev.icerock.gradle.talaiot") version("3.+")
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