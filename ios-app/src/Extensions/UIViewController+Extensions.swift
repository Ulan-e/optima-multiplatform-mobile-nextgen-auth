//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary
import UIKit

extension UIViewController {
    func topPresentedViewController() -> UIViewController? {
        var topController = self.presentedViewController
        while let presentedViewController = topController?.presentedViewController {
            topController = presentedViewController
        }
        return topController
    }
}

// MARK: -  UIViewController livedata bind extension
