//
//  FilledOtpView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct FilledOtpView: View {
    
    private var number: String
    
    public init(number: String) {
        self.number = number
    }
    
    public var body: some View {
        Text(number)
            .font(AppFonts.textFont(weight: .medium, size: .size24))
            .frame(width: AppSizes.otpBoxWidth, height: AppSizes.otpBoxWidth)
            .background(RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                .fill(Color.gray.opacity(OpacityColor.opacity10)))
            .overlay(
                RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                    .stroke(Color.gray.opacity(OpacityColor.opacity50), lineWidth: AppSizes.width1)
            )
    }
}

//struct FilledOtpView_Previews: PreviewProvider {
//    static var previews: some View {
//        FilledOtpView()
//    }
//}
