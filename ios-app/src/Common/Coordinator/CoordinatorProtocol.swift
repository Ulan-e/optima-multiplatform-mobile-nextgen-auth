//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

protocol Coordinator: AnyObject {
    var completionHandler: (() -> Void)? { get }
    func start()
    func clear()
}
