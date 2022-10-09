//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import common
import SwiftUI

@main
struct OptimaApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    var body: some Scene {
        WindowGroup {
            RootView(isLoad: .constant(false))
        }
    }
}
