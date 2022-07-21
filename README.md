# Common info

Project created on base of https://github.com/icerockdev/moko-template.

# Prerequirements

- MacOS for iOS development
- Oracle JDK 11
- Android Studio 4.0+ - https://developer.android.com/studio
- android sdk (setup with android studio)
- xcode 12+ (on macos)
- cocoapods 1.10+ (on macos)
- xcode command line tools (on macos - `xcode-select --install`)

# Setup

## Setup ApplicationId

- `android-app/build.gradle.kts:17`
- `ios-app/BuildConfigurations/ios-app.dev.xcconfig` - with `.dev` postfix
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`
- `ios-app/BuildConfigurations/ios-app.stage.xcconfig` - with `.stage` postfix
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`
- `ios-app/BuildConfigurations/ios-app.prod.xcconfig`
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`

## Setup apps in Firebase

- Install [Firebase CLI](https://firebase.google.com/docs/cli)
- run setup script - `./setup-firebase-apps.sh <firebase_project_id> <bundle_id> <bundle_id_prod>`

Script will create applications with `<bundle_id>.dev`, `<bundle_id>.stage` and `<bundle_id_prod>` for iOS and Android (Android with `*.debug` versions for all of them). 
Different parameter for production bundle needed for cases when you use fully different bundle, not matching with development. If you don't need different bundle - pass `<bundle_id>` and `<bundle_id_prod>` are same values.

- download `google-services.json` to `android-app/google-services.json`
- download all `GoogleService-Info.plist` to `ios-app/src/Firebase/GoogleService-Info-<bundle_id>.plist`

## Setup android signing

- remove `android-app/signing/release.jks`
- Call `./setup-android-signing.sh <key_name> <gitlab_token> <gitlab_project_id>`
- commit and push created file

## Setup Firebase AppDistribution deploy

- run `firebase login:ci`
- copy token from results
- call `./setup-firebase-deploy.sh <gitlab_token> <gitlab_project_id> <firebase_token>`
- In the App Distribution page of the Firebase console, select the app you want to distribute, then click `Get started`
- in `.gitlab-ci.yml` remove dots at start of deploy firebase jobs
- in `.gitlab-ci.yml` remove gitlab distribution jobs

## Setup Firebase TestLab

- Instruction - [Firebase TestLab](https://confluence.icerockdev.com/display/AD/Firebase+testlab)

## Setup GitLab Distribution deploy

- get token from [Confluence](https://confluence.icerockdev.com/pages/viewpage.action?pageId=69437109)
- call `./setup-gitlab-deploy.sh <gitlab_token> <gitlab_project_id> <deploy_token>`

## Setup iOS CI config

- call `./setup-ios-ci.sh <gitlab_token> <gitlab_project_id> <fastlane_password> <match_password>`

# Build

## Build android

Just open in android studio and press run. Or you can build in CLI - `./gradlew assembleDebug`

## Build ios

1. Install cocoapods dependencies `(cd ios-app && pod install)` (first run to give kotlin compiler native dependencies which we use);
1. Build kotlin shared library framework for iOS cocoapods - `./gradlew :mpp-library:syncMultiPlatformLibraryDebugFrameworkIosX64`;
1. Install cocoapods with already compiled kotlin `(cd ios-app && pod install)` (second run to valid integration of kotlin framework in ios xcode project);
1. Open xcode workspace - `open ios-app/ios-app.xcworkspace` and run app.

## iOS build configurations

One of the best practices in iOS development is to be able to manage multiple environments during project development.

The ios-app directory contains the `BuildConfiguration/` folder, where the build settings are located:
- `debug`
- `release`
- `shared`

As well as the assembly stages:
- `dev`
- `stage`
- `prod`

The configurations themselves are obtained by combining the settings.
For example, the `stage-debug` configuration turns out like this:
```bash
#include "../ios-app.stage.xcconfig"
#include "../ios-app.debug.xcconfig"
```

You can find even more build settings [here](https://xcodebuildsettings.com).

## 
After successful run you can change kotlin code and just run app in xcode - all changes of kotlin will be recompiled as needed, or caches will be used. No need `pod install` later or call `sync` gradle task.
