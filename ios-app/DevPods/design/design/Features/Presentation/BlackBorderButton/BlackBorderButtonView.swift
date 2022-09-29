//
//  BlackBorderButtonView.swift
//  biometric
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct BlackBorderButtonView: View {
    private let title: String
    private let action: ((Bool) -> ())?
    
    public init(title: String, action: ((Bool) -> ())?) {
        self.title = title
        self.action = action
    }
    
    public var body: some View {
        Button {
            guard let action = action else { return }
            action(false)
        } label: {
            textView
        }
    }
    
    var textView: some View {
        Text(self.title)
            .foregroundColor(AppColors.getColor(.primeryBlack))
            .frame(maxWidth: .infinity, minHeight: AppSizes.buttonHeight)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .background(Color.clear)
            .overlay(
                RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                    .stroke(AppColors.getColor(.primeryBlack), lineWidth: 1)
            )
    }
}

//struct BlackBorderButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        BlackBorderButtonView()
//    }
//}
