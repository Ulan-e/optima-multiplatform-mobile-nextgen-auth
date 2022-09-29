//
//  RedButtonView.swift
//  biometric
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct RedButtonView: View {
    private let title: String
    private let action: ((Bool) -> ())?
    
    @EnvironmentObject var state: RedButtonState
    
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
            .frame(maxWidth: .infinity, minHeight: 49)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .foregroundColor(state.isActive ? AppColors.getColor(.primaryWhite) : AppColors.getColor(.primaryGray))
            .background(state.isActive ? AppColors.getColor(.primaryRed) : AppColors.getColor(.primaryGrayLight))
            .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}

//struct RedButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        RedButtonView()
//    }
//}
