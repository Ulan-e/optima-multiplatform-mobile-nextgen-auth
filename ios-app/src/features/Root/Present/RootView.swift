//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import common
import design
import MultiPlatformLibrary
import SwiftUI
import UIKit

class RootViewController: UIViewController {
    let contentView = UIHostingController(rootView: RootView(isLoad: .constant(false)))

    override func viewDidLoad() {
        super.viewDidLoad()
        addChild(contentView)
        view.addSubview(contentView.view)
    }
}

struct RootView: View {
    @State var isActive: Bool = false
    @Binding var isLoad: Bool

    var body: some View {
//        LoadingView(isShowing: isLoad) {
        NavigationView {
            ZStack {
                if !isActive {
                    SplashViewController()
                } else {
                    NavigationLink(destination: WelcomeView(isLoad: false), isActive: $isActive) {}
                }
            }
            .ignoresSafeArea(.all)
            .onAppear {
                isLoad.toggle()
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    isLoad.toggle()
                    self.isActive = true
                }
            }
        }
    }
//    }
}

struct SplashViewController: UIViewControllerRepresentable {
    func makeUIViewController(context _: Context) -> some UIViewController {
        let storyboard = UIStoryboard(name: "Root", bundle: Bundle.main)
        let controller = storyboard.instantiateViewController(identifier: "Root")
        return controller
    }

    func updateUIViewController(_: UIViewControllerType, context _: Context) {}
}
