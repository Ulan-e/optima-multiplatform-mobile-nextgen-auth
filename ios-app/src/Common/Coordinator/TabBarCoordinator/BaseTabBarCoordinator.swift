//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import UIKit

/*
 * Base class for coordinator with UITabBarController in root
 */
class BaseTabBarCoordinator: BaseCoordinator {
    private weak var tabBarController: UITabBarController?

    /*
     * Dictionary for controllers, that will be added as childs to tabBarController
     * Key - identifier of screen, used for routes
     * It must be filled, before calling the start method
     */
    private var controllers: [String: UIViewController] = [:]
    /*
     *
     * Array with identifiers, identifiers must match keys in controllers dictionary
     * It used to determine controllers order in UITabBar
     */
    private var identifiers: [String] = []

    /*
     * Default method for showing screen without animation
     */
    override func start() {
        startAnimated(animation: nil)
    }

    /*
     * This method add all child controller and shows UITabBarController
     * animation is used to determine transition animation, don't forget to add controller to hierarchy
     * If animation = nil, UItabBarController will be used as rootViewController for window
     */
    func startAnimated(animation: ((UIViewController) -> Void)?) {
        let strongTabBarController = createTabBarController()
        tabBarController = strongTabBarController

        setupControllers()
        tabBarController?.viewControllers = identifiers.map { controllers[$0]! }
        if animation == nil {
            window.rootViewController = strongTabBarController
        } else {
            animation?(strongTabBarController)
        }
    }

    // Override for using custom UITabBarController
    func createTabBarController() -> UITabBarController {
        UITabBarController()
    }

    /*
     * You must override this method and setup controllers by using 'addControllerToTab' or 'createModule' methods
     * This method calls inside start method
     */
    func setupControllers() {
        fatalError("add controller using create module func ")
    }

    /*
     * Method for route to tab specified with identifier
     * identifier must match key in controllers dictionary
     */
    func routeToTab(with identifier: String) {
        tabBarController?.selectedViewController = controllers[identifier]
    }

    /*
     * Returns coordinator of current active controller
     */
    func getSelectedCoordinator() -> BaseCoordinator? {
        guard let index = tabBarController?.selectedIndex,
              let currentCoordinator: BaseCoordinator = childCoordinators[index] as? BaseCoordinator
        else {
            return nil
        }
        return currentCoordinator
    }

    /*
     * Method for adding custom controller, if you dont want to use createModule method
     */
    func addControllerToTab(id: String, controller: UIViewController) {
        controllers[id] = controller
        identifiers.append(id)
    }

    /*
     * Method for creating child Tabbar controller, in this method will be created UINavigationController,
     *      this controller will be added to controllers array and to coordinator
     * identifier - you can use this id for routeToTab method
     * coordinator - will be used for yhis module navigation, you dont need to call addDependency outside this method
     * title - title for UITabBarItem
     * image - icon for UITabBarItem inactive state
     * selectedImage: icon for UITabBarItem selected state
     * isDefaultStart - if true - coordinator's method `start` will be called,
     *      if you have custom `start` method, set false for this parameter
     */
    @discardableResult
    func createModule(
        identifier: String,
        coordinator: BaseCoordinator,
        title: String,
        image: UIImage?,
        selectedImage: UIImage?,
        isDefaultStart: Bool = true
    ) -> UINavigationController {
        let _ = addDependency(coordinator)
        let navVC = UINavigationController()
        coordinator.navigationController = navVC

        if isDefaultStart {
            coordinator.start()
        }

        navVC.tabBarItem = UITabBarItem(
            title: title,
            image: image,
            selectedImage: selectedImage
        )

        controllers[identifier] = navVC
        identifiers.append(identifier)
        return navVC
    }
}
