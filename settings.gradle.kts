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
include(":mpp-library:feature:auth")
