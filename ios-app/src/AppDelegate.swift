//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import FirebaseCore
import MCRCStaticReporter
import MultiPlatformLibrary
import UIKit

// @UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    var window: UIWindow?

    private(set) var coordinator: AppCoordinator!

    func application(_: UIApplication, didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        FirebaseApp.configure()
        MokoFirebaseCrashlytics.setup()

        let antilog: Antilog?
        #if DEBUG
            antilog = DebugAntilog(defaultTag: "debug")
        #else
            antilog = nil
        #endif

        // create factory of shared module - it's main DI component of application.
        // Provide ViewModels of all features.
        // Input is platform-specific:
        // * settings - settings platform storage for https://github.com/russhwolf/multiplatform-settings
        // * antilog - platform logger with println for https://github.com/AAkira/Napier
        // * baseUrl - server url from platform build configs (allows use schemes for server configs)
        // * newsUnitsFactory - platform factory of UITableView items for https://github.com/icerockdev/moko-units
        AppComponent.factory = SharedFactory(
            settings: AppleSettings(delegate: UserDefaults.standard),
            antilog: antilog,
            baseUrl: Environment.Keys.serverBaseUrl.value()
        )

        let window = UIWindow()

        coordinator = AppCoordinator(
            window: window,
            factory: AppComponent.factory
        )
        coordinator.start()

        window.makeKeyAndVisible()
        self.window = window

        return true
    }
}
