//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary
import UIKit

class BaseViewController<VM: ViewModel>: MVVMController<VM> {
    // Call on viewDidLoad if neededd
    func setupKeyboardObservers() {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(_keyboardWillShow(notification:)),
                                               name: UIResponder.keyboardWillShowNotification, object: nil)

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(_keyboardWillHide(notification:)),
                                               name: UIResponder.keyboardWillHideNotification, object: nil)
    }

    @objc
    private func _keyboardWillShow(notification: Notification) {
        if let frame: CGRect = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect {
            self.keyboardWillShow(frame: frame)
        }
    }

    @objc
    private func _keyboardWillHide(notification _: Notification) {
        self.keyboardWillHide()
    }

    func setActivityIndicatorHidden(_ hidden: Bool) {
        if !hidden {
            self.view.endEditing(true)
            showProgress()
        } else {
            hideProgress()
        }
    }

    func bindLoading(_ liveData: LiveData<KotlinBoolean>) {
        liveData.addObserver { [weak self] loading in
            self?.setActivityIndicatorHidden(!(loading?.boolValue ?? false))
        }
    }

    // TODO: Override if use
    func keyboardWillShow(frame _: CGRect) {}

    // TODO: Override if use
    func keyboardWillHide() {}

    // Implementation depends on project UI and requirements
    func showProgress() {}

    func hideProgress() {}
}
