plugins {
    id("com.android.library")
}

android {

    compileSdkVersion(31)
    defaultConfig {
        minSdk = 23
        targetSdk = 31
    }

//    sourceSets {
//        main {
//            manifest.srcFile 'AndroidManifest.xml'
//            java.srcDirs = ['src']
//            resources.srcDirs = ['src']
//            aidl.srcDirs = ['src']
//            renderscript.srcDirs = ['src']
//            res.srcDirs = ['res']
//            assets.srcDirs = ['assets']
//        }
//    }

    sourceSets.getByName("main"){
        manifest.srcFile("AndroidManifest.xml")
        java.srcDirs("src")
        resources.srcDirs("src")
        aidl.srcDirs("src")
        renderscript.srcDirs("src")
        res.srcDirs("src")
        assets.srcDirs("assets")
    }
}

dependencies {
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}