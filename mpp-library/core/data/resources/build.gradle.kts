import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
	kotlin("multiplatform")
	kotlin("native.cocoapods")
	id("com.android.library")
	id("dev.icerock.mobile.multiplatform-resources")
}

version = "1.0"

kotlin {
	android()
	iosArm64()
	iosSimulatorArm64()
	iosX64()

	explicitApi()

	val xcFramework = XCFramework("resources")
	targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class)
		.matching { it.konanTarget.family == org.jetbrains.kotlin.konan.target.Family.IOS }
		.configureEach {
			binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class)
				.configureEach {
					xcFramework.add(this)
				}
		}


	cocoapods {
		summary = "Some description for the Shared Module"
		homepage = "Link to the Shared Module homepage"
		ios.deploymentTarget = "14.1"
		framework {
			baseName = "resources"
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				val mokoResourcesVersion = "0.20.1"
				implementation("dev.icerock.moko:resources:$mokoResourcesVersion")
				implementation("dev.icerock.moko:graphics:0.9.0")
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
			}
		}
		val androidMain by getting {
			dependencies {
				val composeVersion = "1.3.0-alpha01"
				implementation("androidx.compose.animation:animation:$composeVersion")
				implementation("androidx.compose.foundation:foundation:$composeVersion")
				implementation("androidx.compose.material:material:$composeVersion")
				implementation("androidx.compose.runtime:runtime:$composeVersion")
				implementation("androidx.compose.ui:ui:$composeVersion")
			}
		}
		val androidTest by getting
		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting
		val iosMain by creating {
			dependsOn(commonMain)
			iosX64Main.dependsOn(this)
			iosArm64Main.dependsOn(this)
			iosSimulatorArm64Main.dependsOn(this)
		}
		val iosX64Test by getting
		val iosArm64Test by getting
		val iosSimulatorArm64Test by getting
		val iosTest by creating {
			dependsOn(commonTest)
			iosX64Test.dependsOn(this)
			iosArm64Test.dependsOn(this)
			iosSimulatorArm64Test.dependsOn(this)
		}
	}
}

android {
	compileSdk = 32
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdk = 21
		targetSdk = 32
	}
}

multiplatformResources {
	multiplatformResourcesPackage = "kg.optima.mobile"
	disableStaticFrameworkWarning = true
}
