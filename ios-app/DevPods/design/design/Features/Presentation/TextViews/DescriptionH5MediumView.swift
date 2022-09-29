//
//  DescriptionH5MediumView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct DescriptionH5MediumView: View {
    private let title: String
    private let color: Color
    
    public init(title: String, color: Color = AppColors.getColor(.primeryBlack)) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .medium, size: .size14))
    }
}

//struct DescriptionH5MediumView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionH5MediumView()
//    }
//}
