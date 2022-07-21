compile() {
  # detekt
  ./gradlew detektWithoutTests &&
  # faster way - android compilation and tests
  ./gradlew assembleDevDebug testDevDebugUnitTest &&
  # check ios compilation and tests
  ./gradlew compileKotlinIosX64 iosX64Test &&
  # check ios framework compilation
  ./gradlew syncMultiPlatformLibraryDebugFrameworkIosX64
}

time compile
