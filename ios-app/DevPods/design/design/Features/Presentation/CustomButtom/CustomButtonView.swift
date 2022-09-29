//
//  CustomButtonView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct CustomButtonView: View {
    private let title: String
    private let color: Color
    private let action: ((Bool) -> ())?
    
    @StateObject var state = CustomButtonState()
    
    public init(title: String,
                color: Color = AppColors.getColor(.primeryBlack),
                action: ((Bool) -> ())?
    ) {
        self.title = title
        self.color = color
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
            .foregroundColor(color)
            .frame(maxWidth: .infinity, minHeight: 49, maxHeight: 50)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .background(Color.clear)
    }
}


//struct CustomButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        CustomButtonView()
//    }
//}
