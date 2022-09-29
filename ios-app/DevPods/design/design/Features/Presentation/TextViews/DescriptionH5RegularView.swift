//
//  DescriptionH5RegularView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct DescriptionH5RegularView: View {
    private let title: String
    private let color: Color
    
    public init(title: String,
                color: Color = AppColors.getColor(.primaryGrayDark)
    ) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .regular, size: .size14))
    }
}

//struct DescriptionH5RegularView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionH5RegularView()
//    }
//}
