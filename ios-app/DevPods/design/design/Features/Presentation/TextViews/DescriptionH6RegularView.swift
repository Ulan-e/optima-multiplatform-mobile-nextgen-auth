//
//  DescriptionH6RegularView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct DescriptionH6RegularView: View {
    private let title: String
    private let color: Color
    
    public init(title: String, color: Color = AppColors.getColor(.primeryBlack)) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .regular, size: .size16))
            .lineLimit(100)
    }
}

//struct DescriptionH6RegularView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionH6RegularView()
//    }
//}
