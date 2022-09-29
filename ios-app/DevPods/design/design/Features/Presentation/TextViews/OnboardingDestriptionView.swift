//
//  ImageTextView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct OnboardingDestriptionView: View {
    private let title: String
    private let image: AppImage
    
    public init(title: String, image: AppImage) {
        self.title = title
        self.image = image
    }
    
    public var body: some View {
        HStack(alignment: .top, spacing: AppSizes.spacing16) {
            AppImages.getImage(image)
            DescriptionH5MediumView(title: title)
        }
        .padding(.bottom, AppSizes.paddingTop24)
    }
}

//struct ImageTextView_Previews: PreviewProvider {
//    static var previews: some View {
//        OnboardingDestriptionView()
//    }
//}
