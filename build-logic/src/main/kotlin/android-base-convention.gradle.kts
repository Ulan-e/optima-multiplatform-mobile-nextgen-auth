import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(32)

    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}
