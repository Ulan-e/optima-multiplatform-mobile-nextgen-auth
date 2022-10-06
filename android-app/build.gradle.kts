plugins {
    id("android-app-convention")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")

    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

android {
    buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = "kg.optima.mobile"

        versionCode = Integer.parseInt(project.property("VERSION_CODE") as String)
        versionName = project.property("VERSION_NAME") as String

        val url = "https://newsapi.org/v2/"
        buildConfigField("String", "BASE_URL", "\"$url\"")
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-rc02"
    }
}

kapt {
    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

dependencies {
    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.recyclerView)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.lifecycleRuntime)
    implementation(libs.mokoMvvmViewBinding)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseCrashlytics)
    // Hilt
    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    implementation(projects.mppLibrary)

    implementation(project(":mpp-library:base")) // needed to use feature modules, remove when state machine will be done
    implementation(project(":mpp-library:core"))
    implementation(project(":mpp-library:core:presentation:design-system:android"))
    implementation(project(":mpp-library:feature"))
    implementation(project(":mpp-library:feature:auth"))
    implementation(project(":mpp-library:feature:common"))
    implementation(project(":mpp-library:feature:registration"))

    // Koin
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation("io.insert-koin:koin-android:3.1.4")
    implementation("io.insert-koin:koin-androidx-compose:3.1.4")

    val composeVersion = "1.3.0-alpha01"
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")

    val accompanistVersion = "0.25.1"
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    val mokoResourcesVersion = "0.20.1"
    implementation("dev.icerock.moko:resources-compose:$mokoResourcesVersion")

    val voyagerVersion = "1.0.0-rc02"
    implementation("cafe.adriel.voyager:voyager-core:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")

    implementation("com.arkivanov.decompose:decompose:0.8.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.8.0")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar","*.aar"))))

    implementation(files("libs/veridoc-module-release-v1.16.1.aar"))
    implementation(files("libs/liveness-module-v1.13.4.aar"))

    implementation("androidx.activity:activity-ktx:1.5.1")

    implementation("io.fotoapparat:fotoapparat:2.7.0")
    implementation("com.sdsmdg.harjot:vectormaster:1.1.3")
}