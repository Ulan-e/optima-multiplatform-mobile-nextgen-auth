//
//  GreenButtonView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct GreenButtonView: View {
    private let title: String
    private let action: ((Bool) -> ())?
    
    @EnvironmentObject var state: GreenButtonState
    
    public init(title: String, action: ((Bool) -> ())?) {
        self.title = title
        self.action = action
    }
    
    public var body: some View {
        Button {
            guard let action = action else { return }
            action(state.isActive)
        } label: {
            textView
        }
    }
    
    var textView: some View {
        Text(self.title)
            .foregroundColor(state.isActive ? AppColors.getColor(.primaryWhite) : AppColors.getColor(.primaryGray))
            .frame(maxWidth: .infinity, minHeight: 49, maxHeight: 50)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .background(state.isActive ? AppColors.getColor(.secondaryGreen) : AppColors.getColor(.primaryGrayLight))
            .clipShape(RoundedRectangle(cornerRadius: AppSizes.standartCorner))
    }
}

//struct GreenButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        GreenButtonView()
//    }
//}
