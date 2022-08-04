plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("kotlin-android")
	id("dev.icerock.mobile.multiplatform-resources")
}

android {
	compileSdk = 32

	defaultConfig {
		minSdk = 21
		targetSdk = 32

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.2.0-rc02"
	}
}

dependencies {
	implementation(project(":mpp-library"))

	implementation("androidx.core:core-ktx:1.8.0")
	implementation("androidx.appcompat:appcompat:1.4.2")
	implementation("com.google.android.material:material:1.6.1")

	val composeVersion = "1.3.0-alpha01"
	implementation("androidx.compose.animation:animation:$composeVersion")
	implementation("androidx.compose.foundation:foundation:$composeVersion")
	implementation("androidx.compose.material:material:$composeVersion")
	implementation("androidx.compose.runtime:runtime:$composeVersion")
	implementation("androidx.compose.ui:ui:$composeVersion")
	implementation("androidx.activity:activity-compose:1.5.0")
	implementation("androidx.compose.ui:ui-tooling:1.3.0-alpha02")
	implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")

	val mokoResourcesVersion = "0.20.1"
	implementation("dev.icerock.moko:resources:$mokoResourcesVersion")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.3")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}