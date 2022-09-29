//
//  SwiftUIView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 27/9/22.
//

import SwiftUI

public struct LoadingView<Content>: View where Content: View {

    @State var isShowing: Bool
//    @Binding var isShowing: Bool
    var content: () -> Content
    
    public init(isShowing: Bool = false, content: @escaping () -> Content) {
        self.isShowing = isShowing
        self.content = content
    }
    
    public var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .center) {
                
                self.content()
                    .disabled(self.isShowing)
                    .blur(radius: self.isShowing ? 3 : 0)

                VStack {
                    ActivityIndicator()
                        .frame(width: AppSizes.width40, height: AppSizes.height40)
                        .foregroundColor(AppColors.getColor(.primaryRed))
                }
                .frame(width: AppSizes.width50, height: AppSizes.height50)
                .background(AppColors.getColor(.primaryWhite))
                .cornerRadius(AppSizes.corner16)
                .opacity(self.isShowing ? 1 : 0)

            }
        }
    }

}

