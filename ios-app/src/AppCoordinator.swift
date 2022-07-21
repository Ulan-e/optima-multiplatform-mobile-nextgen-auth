//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import UIKit

class AppCoordinator: BaseCoordinator {
    override func start() {
        let tabBarCoordinator = addDependency(ExampleTabBarCoordinator(window: window, factory: factory))
        tabBarCoordinator.start()
    }
}

// Example of using BaseTabBarCoordinator
class ExampleTabBarCoordinator: BaseTabBarCoordinator {
    enum TabId: String {
        case menu
        case profile
        static func values() -> [TabId] {
            return [menu, profile]
        }
    }

    override func setupControllers() {
        TabId.values().forEach { id in
            switch id {
            case .menu:
                makeMenuModule()
            case .profile:
                makeProfileModule()
            }
        }
    }

    private func makeMenuModule() {
        if #available(iOS 13.0, *) { // #available for using UIImage(systemName:)
            let menuNavController = createModule(identifier: TabId.menu.rawValue,
                                                 coordinator: BaseCoordinator(window: window, factory: factory),
                                                 title: "Menu",
                                                 image: UIImage(systemName: "heart.fill"),
                                                 selectedImage: UIImage(systemName: "heart.fill"))

            // --- This shoud be in child coordinartor's start method
            let menuController = UIViewController()
            menuNavController.viewControllers = [menuController]
            menuController.view.backgroundColor = .white
            menuController.navigationItem.title = "Menu"
            menuController.navigationItem.rightBarButtonItem = UIBarButtonItem(
                title: "Profile",
                style: .plain,
                target: self,
                action: #selector(exampleRouteToProfile)
            )
            // ---
        }
    }

    private func makeProfileModule() {
        if #available(iOS 13.0, *) { // #available for using UIImage(systemName:)
            let profileNavController = createModule(identifier: TabId.profile.rawValue,
                                                    coordinator: BaseCoordinator(window: window, factory: factory),
                                                    title: "Profile",
                                                    image: UIImage(systemName: "airplane.arrival"),
                                                    selectedImage: UIImage(systemName: "airplane.arrival"))

            // --- This shoud be in child coordinartor's start method
            let profileController = UIViewController()
            profileController.view.backgroundColor = .white
            profileNavController.viewControllers = [profileController]
            profileController.navigationItem.title = "Profile"
            // ---
        }
    }

    @objc private func exampleRouteToProfile() {
        routeToTab(with: TabId.profile.rawValue)
    }
}
