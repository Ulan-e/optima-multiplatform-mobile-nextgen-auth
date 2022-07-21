//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import UIKit

extension BaseCoordinator {
    func showDefaultAlertDialog(title: String?, message: String?) {
        let alert = UIAlertController(title: title,
                                      message: message,
                                      preferredStyle: .alert)

        navigationController?.present(alert, animated: true)
    }
}
