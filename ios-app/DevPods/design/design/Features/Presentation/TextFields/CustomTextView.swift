//
//  CustomTextView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import SwiftUI

public struct CustomTextView: View {
    
    private let palceholder: String
    @State private var answer = ""
    private let action: ((String) -> ())?
    
    public init(palceholder: String,
                answer: String = "",
                action: ((String) -> ())?
    ) {
        self.palceholder = palceholder
        self.answer = answer
        self.action = action
    }
    
    public var body: some View {
        TextField(palceholder, text: $answer)
            .padding()
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .foregroundColor(AppColors.getColor(.primeryBlack))
            .background(AppColors.getColor(.primaryGrayDark, opacity: OpacityColor.opacity08))
            .cornerRadius(AppSizes.standartCorner)
            .padding(.top, AppSizes.paddingTop24)
            .onChange(of: answer) { newValue in
                guard let action = action else { return }
                action(newValue)
            }
    }
}

//struct CustomTextView_Previews: PreviewProvider {
//    static var previews: some View {
//        CustomTextView()
//    }
//}
