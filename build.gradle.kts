
import org.gradle.api.internal.artifacts.DefaultModuleVersionSelector

plugins {
    id("com.github.jakemarsden.git-hooks") version "0.0.2"
    id("dev.icerock.moko.kswift") version "0.5.0"
}

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url  = uri("https://jitpack.io") }

        flatDir {
            dirs("libs")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath(libs.mokoResourcesGeneratorGradle)
        classpath(libs.mokoNetworkGeneratorGradle)
        classpath(libs.mokoUnitsGeneratorGradle)
        classpath(libs.kotlinSerializationGradle)
        classpath(libs.hiltGradle)
        classpath(libs.firebaseCrashlyticsGradle)
        classpath(libs.googleServicesGradle)
        classpath(":build-logic")

        classpath("io.realm.kotlin:gradle-plugin:1.2.0")
    }
}

gitHooks {
    setHooks(mapOf("pre-push" to "detektWithoutTests"))
}

allprojects {
    configurations.configureEach {
        resolutionStrategy {
            val coroutines: MinimalExternalModuleDependency = rootProject.libs.coroutines.get()
            val forcedCoroutines: ModuleVersionSelector = DefaultModuleVersionSelector.newSelector(
                coroutines.module,
                coroutines.versionConstraint.requiredVersion
            )
            force(forcedCoroutines)
        }
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
