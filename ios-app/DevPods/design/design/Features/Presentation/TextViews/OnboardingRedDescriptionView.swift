//
//  OnboardingRedDescriptionView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct OnboardingRedDescriptionView: View {
    private let title: String
    private let subTitle: String
    private let link: String
    private let image: AppImage
    private let action: ((Bool) -> ())?
    
    public init(title: String,
                subTitle: String,
                link: String,
                image: AppImage,
                action: ((Bool) -> ())?) {
        self.title = title
        self.subTitle = subTitle
        self.link = link
        self.image = image
        self.action = action
    }
    
    public var body: some View {
        HStack(alignment: .top, spacing: AppSizes.spacing16) {
            AppImages.getImage(image)
            ParagraphTextView(title: title,
                              subTitle: subTitle,
                              link: link,
                              color: AppColors.getColor(.primeryBlack),
                              subColor: AppColors.getColor(.primaryRed),
                              font: AppFonts.textFont(weight: .medium, size: .size14),
                              action: action
            )
        }
    }
}

//struct OnboardingRedDescriptionView_Previews: PreviewProvider {
//    static var previews: some View {
//        OnboardingRedDescriptionView()
//    }
//}
