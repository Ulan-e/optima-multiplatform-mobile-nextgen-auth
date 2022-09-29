//
//  CheckboxView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct CheckboxView: View {
    private let title: String
    private let action: ((Bool) -> ())?
    
    @StateObject private var state = CheckboxState()
    
    public init(title: String, action: ((Bool) -> ())?) {
        self.title = title
        self.action = action
    }
    
    public var body: some View {
        HStack {
            withAnimation {
                AppImages.getImage(state.isActive ? AppImage.checkboxOn : AppImage.checkboxOff)
                    .background(Color.clear)
                    .frame(width: AppSizes.checkBoxWidth, height: AppSizes.checkBoxHeight)
            }
                
            Text(title)
                .font(AppFonts.textFont(weight: .regular, size: .size16))
                .foregroundColor(AppColors.getColor(.primaryGrayDark))
        }.onTapGesture {
            state.isActive.toggle()
            
            guard let action = self.action else { return }
            action(self.state.isActive)
        }
    }
}

