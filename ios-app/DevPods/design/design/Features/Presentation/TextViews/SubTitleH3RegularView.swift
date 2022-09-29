//
//  SubTitleH3RegularView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 23/9/22.
//

import SwiftUI

public struct SubTitleH3RegularView: View {
    private let title: String
    private let color: Color
    
    public init(title: String,
                color: Color = AppColors.getColor(.primeryBlack)) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .regular, size: .size18))
    }
}

//struct SubTitleH3RegularView_Previews: PreviewProvider {
//    static var previews: some View {
//        SubTitleH3RegularView()
//    }
//}
