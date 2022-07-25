//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Foundation
import MultiPlatformLibrary
import UIKit

class BaseCoordinator: NSObject, Coordinator, UINavigationControllerDelegate {
    /*
     * Array of child coordinators you can have
     */
    var childCoordinators: [Coordinator] = []
    /*
     * Completion when you need to configure what should happens when
     * coordinator is deiniting.
     */
    var completionHandler: (() -> Void)?
    fileprivate var clearHandler: (() -> Void)?

    /*
     * Main coordinator window
     */
    let window: UIWindow
    /*
     * Multiplatform shared factory
     */
    let factory: SharedFactory
    /*
     * Coordinator navigation controller
     */
    var navigationController: UINavigationController?

    init(window: UIWindow, factory: SharedFactory) {
        self.window = window
        self.factory = factory
    }

    /*
     * Adds passed coordinator as a child to base one.
     *
     * Call this method when you need to display new feature-controller.
     */
    func addDependency<Child>(_ coordinator: Child, completion: (() -> Void)? = nil) -> Child where Child: BaseCoordinator {
        for element in childCoordinators.compactMap({ $0 as? Child }) {
            if element === coordinator { return element }
        }
        coordinator.completionHandler = { [weak self, weak coordinator] in
            self?.removeDependency(coordinator)
            completion?()
        }
        
        childCoordinators.append(coordinator)
        return coordinator
    }

    /*
     * Calls every child coordinator completion clear handler.
     *
     * Usually use it on logout or on deeplink handling.
     * Because if this isn't done, coordinators may remain in memory, which will lead to leaks.
     */
    func clear() {
        clearHandler?()
        childCoordinators.forEach {
            $0.clear()
        }
        childCoordinators.removeAll()
    }

    /*
     * Erases coordinators dependencies stack.
     */
    private func removeDependency(_ coordinator: Coordinator?) {
        clearHandler?()
        guard
            childCoordinators.isEmpty == false,
            let coordinator = coordinator
        else { return }

        for (index, element) in childCoordinators.enumerated() {
            if element === coordinator {
                childCoordinators.remove(at: index)
                break
            }
        }
    }

    // Cases
    // 1. Initial with window - create NV, etc..
    // 2. Exists navcontroller,

    /*
     * Implement this method in each inheritor as an initial method.
     *
     * You should also call it after adding coordinator to dependencies.
     */
    func start() {
        //
    }

    /*
     * Creates new navigation controller and makes it as a new root.
     *
     * Use it when you need to add new controller (based on design and business logic)
     * with its own navigation.
     */
    func beginInNewNavigation(_ controller: UIViewController) -> UINavigationController {
        let newNavigationController = UINavigationController()
        self.navigationController = newNavigationController

        newNavigationController.setViewControllers([controller], animated: false)

        self.window.rootViewController = newNavigationController

        self.clearHandler = { [weak self] in
            // get controllers and view models, clear them
            self?.popToRoot()
        }

        return newNavigationController
    }

    /*
     * Pushes passed view controller and saves currently displayed view
     * controller on the top of the stack to route back after new controller
     * will be erased.
     *
     * Use it when you need to add controller (based on design and business logic)
     * based on exists navigation.
     */
    func beginInExistNavigation(_ controller: UIViewController) {
        let prevController = self.navigationController?.topViewController
        self.clearHandler = { [weak self, weak prevController] in
            // get controllers and view models, clear them
            if let prev = prevController {
                self?.popToViewController(controller: prev)
            }
        }
        navigationController?.pushViewController(controller, animated: true)
    }

    /*
     * If current view controller is the las one in the controllers stack
     * popViewController returns nil. Related to popViewController result
     * this method erases all view models in the stack or if result is nil
     * navigation controller just closes.
     */
    private func popBack() {
        let popVC = self.navigationController?.popViewController(animated: true)
        if let nVC = popVC {
            clearViewModels(forControllers: [nVC])
        } else {
            navigationController?.dismiss(animated: true, completion: nil)
        }
    }

    /*
     * Erases all view models for specific controllers.
     */
    private func clearViewModels(forControllers controllers: [UIViewController]?) {
        let holders = (controllers ?? []).compactMap { $0 as? ViewModelHolder }
        holders.forEach { $0.baseViewModel?.onCleared() }
    }

    /*
     * Closes view controller presented modally.
     * Get view controllers from navigation view stack and erase theirs view models.
     */
    private func dismissModal() {
        let controllers = navigationController?.viewControllers
        navigationController?.dismiss(animated: true, completion: nil)
        clearViewModels(forControllers: controllers)
    }

    /*
     * Go back to previous view controller
     */
    private func popToViewController(controller vc: UIViewController, animated: Bool = true) {
        let controllers = navigationController?.popToViewController(vc, animated: animated)
        clearViewModels(forControllers: controllers)
    }

    /*
     * Go back to view controller of specific class and erase view models
     */
    private func popToViewController(ofClass: AnyClass, animated: Bool = true) {
        if let vc = navigationController?.viewControllers.last(where: { $0.isKind(of: ofClass) }) {
            let controllers = navigationController?.popToViewController(vc, animated: animated)
            clearViewModels(forControllers: controllers)
        }
    }

    /*
     * Get root view controller and erase view models
     */
    private func popToRoot() {
        let controllers = navigationController?.popToRootViewController(animated: true)
        clearViewModels(forControllers: controllers)
    }

    /*
     * Get viewcontroller from current navigation hierarchy.
     *
     * Use it to get access for ViewController class methods and field from coordinator routing methods.
     * The most common usage - display UIAlertView modally.
     */
    func currentViewController() -> UIViewController {
        guard let navController = self.navigationController else { return UIViewController() }
        return navController.topViewController ?? navController.topPresentedViewController() ?? navController
    }
}
