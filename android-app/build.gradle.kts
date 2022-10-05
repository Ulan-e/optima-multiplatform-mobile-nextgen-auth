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

    implementation("com.arkivanov.decompose:decompose:1.0.0-alpha-04")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-alpha-04")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar","*.aar"))))

    implementation(files("libs/veridoc-module-release-v1.16.1.aar"))
    implementation(files("libs/liveness-module-v1.13.4.aar"))

    implementation("androidx.activity:activity-ktx:1.5.1")

    implementation("io.fotoapparat:fotoapparat:2.7.0")
    implementation("com.sdsmdg.harjot:vectormaster:1.1.3")

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.preference:preference:1.2.0")
    implementation("com.github.skydoves:balloon:1.4.5")
    implementation("ru.egslava:MaskedEditText:1.0.5")

    // UI
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("androidx.fragment:fragment:1.4.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    // GCM
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-maps:18.0.0")

    // RxJava3
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    // Room
    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-rxjava2:2.4.2")
    annotationProcessor("androidx.room:room-compiler:2.4.2")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.3.2")
    implementation("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")

   // implementation("ir.beigirad:ZigzagView:1.2.0")
    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")

    implementation("com.alibaba.android:ultraviewpager:1.0.7.7@aar")
    implementation("com.isseiaoki:simplecropview:1.1.7")
    implementation("com.akexorcist:RoundCornerProgressBar:2.0.3")
  // implementation("cards.pay:paycardsrecognizer:1.1.0")

    implementation("me.leolin:ShortcutBadger:1.1.4@aar")
    implementation("com.jakewharton:butterknife:10.0.0")
    implementation("devlight.io:navigationtabbar:1.2.5")
    implementation("fr.avianey.com.viewpagerindicator:library:2.4.1.1@aar")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.multidex:multidex:2.0.0")
    implementation("com.tubb.smrv:swipemenu-recyclerview:5.4.0")
    implementation("me.henrytao:smooth-app-bar-layout:25.3.1.0")
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("com.daimajia.slider:library:1.1.5@aar")
    implementation("com.wang.avi:library:2.1.3")
    implementation("com.redmadrobot:inputmask:2.3.0")
    implementation("com.wang.avi:library:2.1.3")//ПрогрессБар
    implementation("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")
    implementation("com.redmadrobot:inputmask:2.3.0")  //Маска
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.legacy:legacy-support-v13:1.0.0")

    implementation("com.shuhart.stepview:stepview:1.5.1")
    implementation("org.greenrobot:eventbus:3.1.1")
    implementation("androidx.lifecycle:lifecycle-process:2.2.0")
    implementation("androidx.annotation:annotation:1.5.0")
    annotationProcessor("com.jakewharton:butterknife-compiler:10.0.0")
    implementation("com.uttampanchasara.pdfgenerator:pdfgenerator:1.3")
}