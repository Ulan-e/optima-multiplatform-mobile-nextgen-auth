//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary
import UIKit

protocol ViewModelHolder {
    var baseViewModel: ViewModel? { get }
}

class MVVMController<VM: ViewModel>: UIViewController {
    private(set) var viewModel: VM?

    var deinitCallback: (() -> Void)?

    func bindViewModel(_ viewModel: VM) {
        loadViewIfNeeded()

        self.viewModel = viewModel
    }

    override func didMove(toParent parent: UIViewController?) {
        if parent == nil {
            let vm: VM? = viewModel
            DispatchQueue.main.async { [weak vm] in
                vm?.onCleared()
            }
        }
    }

    deinit {
        print("***DEINIT: \(self))")
        deinitCallback?()
    }
}

extension MVVMController {
    func bindControl(control: UIControl, _ event: UIControl.Event, action: ((VM) -> Void)?) {
        UIControlExtKt.setEventHandler(control, event: UInt64(event.rawValue), lambda: { [weak self] _ in
            guard let nVm = self?.viewModel else { return }
            action?(nVm)
        })
    }
}

extension MVVMController: ViewModelHolder {
    var baseViewModel: ViewModel? {
        self.viewModel
    }
}
