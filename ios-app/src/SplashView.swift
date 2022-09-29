//
// Copyright (c) 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import common
import design
import SwiftUI

struct SplashView: View {
    @State var isActive: Bool = false
    @Binding var isLoad: Bool

    var body: some View {
//        LoadingView(isShowing: isLoad) {
        NavigationView {
            ZStack(alignment: .center) {
                if !isActive {
                    AppImages.getImage(.logoApp)
                        .imageScale(.large)
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

struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView(isLoad: .constant(false))
    }
}
