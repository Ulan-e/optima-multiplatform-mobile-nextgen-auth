//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import UIKit

class Environment {
    enum Keys: String {
        case serverBaseUrl

        func value() -> String {
            return Environment.bundleDict[self.rawValue] as? String ?? ""
        }
    }

    private static let bundleDict = (Bundle.main.infoDictionary?["Environment"] as? [String: Any] ?? [:])
}
