//
//  DescriptionH1View.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct DescriptionH4RegularView: View {
    private let title: String
    
    public init(title: String) {
        self.title = title
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(AppColors.getColor(.primeryBlack))
            .font(AppFonts.textFont(weight: .regular, size: .size16))
            .lineLimit(100)
    }
}

//struct DescriptionView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionH4RegularView()
//    }
//}
