//
//  PharagraphView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 23/9/22.
//

import SwiftUI

public struct PharagraphAnimView: View {
    private let title: String
    private let image: AppImage
    private let duration: Double
    
    @State var fadeOut = false
    
    public init(title: String,
                image: AppImage,
                duration: Double) {
        self.title = title
        self.image = image
        self.duration = duration
    }
    
    public var body: some View {
        HStack(alignment: .top, spacing: AppSizes.spacing16) {
            AppImages.getImage(image)
            DescriptionH4RegularView(title: title)
        }
        .padding(.bottom, AppSizes.paddingTop24)
        .onAppear(perform: {
            DispatchQueue.main.asyncAfter(deadline: .now() + duration) {
                withAnimation(.easeInOut(duration: 0.3)) {
                    fadeOut.toggle()
                }
            }
        }).opacity(fadeOut ? 1 : 0)
    }
}

//struct PharagraphView_Previews: PreviewProvider {
//    static var previews: some View {
//        PharagraphView()
//    }
//}
