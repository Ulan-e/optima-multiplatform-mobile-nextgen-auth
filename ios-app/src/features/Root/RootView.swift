//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import common
import design
import SwiftUI
import UIKit

class RootViewController: UIHostingController<RootView> {
    override func viewDidLoad() {
        super.viewDidLoad()
    }
}

struct RootView: View {
    @State var isActive: Bool = false
    @Binding var isLoad: Bool

    var body: some View {
//        LoadingView(isShowing: isLoad) {
        NavigationView {
            ZStack(alignment: .center) {
                if !isActive {
                    SplashViewController()
                } else {
                    NavigationLink(destination: WelcomeView(isLoad: false), isActive: $isActive) {}
                }
            }
            .padding()
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

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(isLoad: .constant(false))
    }
}

struct SplashViewController: UIViewControllerRepresentable {
    func makeUIViewController(context _: Context) -> some UIViewController {
        let storyboard = UIStoryboard(name: "Root.storyboard", bundle: Bundle.main)
        let controller = storyboard.instantiateViewController(identifier: "RootViewController")
        return controller
    }

    func updateUIViewController(_: UIViewControllerType, context _: Context) {}
}
