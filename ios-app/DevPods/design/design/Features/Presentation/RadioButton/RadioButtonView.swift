//
//  RadioButtonView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct RadioButtonView: View {
    
    private let id: String
    private let title: String
    
    private let action: ((String) -> ())?
    
    @StateObject var state = RadioButtonState()
    
    public init(
        id: String,
        title: String = "",
        action: ((String) -> ())?
        ) {
        self.id = id
        self.title = title
        self.action = action
    }
    
    public var body: some View {
        Button {
            guard let action = action else { return }
            action(self.id)
        } label: {
            HStack(alignment: .center, spacing: 10) {
                AppImages.getImage(state.isActive ? AppImage.radioOn : AppImage.radioOff)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                
                Text(title)
                    .font(AppFonts.textFont(weight: .medium, size: .size16))
                    .foregroundColor(AppColors.getColor(.primaryGray))
                Spacer()
            }.foregroundColor(Color.clear)
        }
    }
    
    
}

//struct RadioButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        RadioButtonView()
//    }
//}
