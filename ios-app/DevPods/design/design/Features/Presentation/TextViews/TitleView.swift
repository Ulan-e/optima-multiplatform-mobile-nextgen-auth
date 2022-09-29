//
//  TitleView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct TitleView: View {
    private let title: String
    private let size: FontSize
    private let color: Color
    
    public init(title: String,
                size: FontSize = .size20,
                color: Color = AppColors.getColor(.primeryBlack)
    ) {
        self.title = title
        self.size = size
        self.color = color
    }
    
    public var body: some View {
        Text(title)
            .font(AppFonts.textFont(weight: .bold, size: size))
            .foregroundColor(color)
    }
}
