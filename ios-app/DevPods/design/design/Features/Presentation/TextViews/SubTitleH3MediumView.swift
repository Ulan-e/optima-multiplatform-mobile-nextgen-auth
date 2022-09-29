//
//  SubTitleH3MediumView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 26/9/22.
//

import SwiftUI

struct SubTitleH3MediumView: View {
    
    private let title: String
    private let color: Color
    
    public init(title: String,
                color: Color = AppColors.getColor(.primeryBlack)
    ) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .medium, size: .size18))
    }
}

//struct SubTitleH3MediumView_Previews: PreviewProvider {
//    static var previews: some View {
//        SubTitleH3MediumView(title: "")
//    }
//}
