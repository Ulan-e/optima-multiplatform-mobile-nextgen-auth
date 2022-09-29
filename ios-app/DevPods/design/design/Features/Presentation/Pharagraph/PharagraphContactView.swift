//
//  PharagraphContactView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 26/9/22.
//

import SwiftUI

public struct PharagraphContactView: View {
    
    private let image: AppImage
    private let description: String
    private let adittional: String
    
    public init(image: AppImage,
         description: String,
                adittional: String
    ) {
        self.image = image
        self.description = description
        self.adittional = adittional
    }
    
    public var body: some View {
        
        HStack(alignment: .top, spacing: AppSizes.spacing16) {
            AppImages.getImage(image)
            
            VStack(alignment: .leading, spacing: AppSizes.spacing6) {
                DescriptionH5RegularView(title: description, color: AppColors.getColor(.secondaryDark))
                SubTitleH3MediumView(title: adittional)
            }
        }
    }
}

//struct PharagraphContactView_Previews: PreviewProvider {
//    static var previews: some View {
//        PharagraphContactView()
//    }
//}
