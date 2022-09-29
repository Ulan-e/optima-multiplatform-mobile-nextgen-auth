//
//  FocusedOtpView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct FocusedOtpView: View {
    
    @State private var opacity: Double
    
    public init(opacity: Double = 0) {
        self.opacity = opacity
    }
    
    public var body: some View {
        
        Button {
        } label: {
            Text("|")
                .font(AppFonts.textFont(weight: .medium, size: .size24))
                .foregroundColor(Color.gray)
                .blinking(duration: 0.7)
        }
        .frame(width: AppSizes.otpBoxWidth, height: AppSizes.otpBoxWidth)
        .background(RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                        .fill(Color.gray.opacity(OpacityColor.opacity10)))
        .overlay(
            RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                .stroke(Color.gray.opacity(OpacityColor.opacity50), lineWidth: AppSizes.width1)
        )
    }
}

extension View {
    func blinking(duration: Double = 0.75) -> some View {
        modifier(OtpBlinkViewModifier(duration: duration))
    }
}
