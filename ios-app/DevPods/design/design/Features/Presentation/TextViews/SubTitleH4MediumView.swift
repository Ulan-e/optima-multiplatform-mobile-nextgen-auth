//
//  SubTitleH4MediumView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct SubTitleH4MediumView: View {
    private let title: String
    private let color: Color
    
    public init(title: String, color: Color) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .foregroundColor(color)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
    }
}

//struct SubTitleH4MediumView_Previews: PreviewProvider {
//    static var previews: some View {
//        SubTitleH4MediumView()
//    }
//}
