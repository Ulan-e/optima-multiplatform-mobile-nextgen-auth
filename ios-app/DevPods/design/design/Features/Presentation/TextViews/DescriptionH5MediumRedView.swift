//
//  DescriptionH5MediumRedView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI
import UIKit

public struct DescriptionH5MediumRedView: View {
    private let title: String
    private let subTitle: String
    private let color: Color
    private let subColor: Color
    private let action: ((Bool) -> ())?
    
    public init(title: String,
                subTitle: String,
                color: Color = AppColors.getColor(.primeryBlack),
                subColor: Color = AppColors.getColor(.primeryBlack),
                action: ((Bool) -> ())?
                
    ) {
        self.title = title
        self.subTitle = subTitle
        self.color = color
        self.subColor = subColor
        self.action = action
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .medium, size: .size14))
    }
}

//struct DescriptionH5MediumRedView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionH5MediumRedView()
//    }
//}
