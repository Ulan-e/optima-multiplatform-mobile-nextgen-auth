//
//  WelcomeButtonView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct WelcomeButtonView: View {
    private let title: String
    private let image: Image
    private let action: ((Bool) -> ())?
    
    public init(title: String,
                image: Image,
                action: ((Bool) -> ())?
    ) {
        self.title = title
        self.image = image
        self.action = action
    }
    
    public var body: some View {
        VStack(spacing: AppSizes.spacing15) {
            Button {
                guard let action = action else { return }
                action(false)
            } label: {
                image
                    .resizable()
                    .frame(width: AppSizes.welcomeButtonWidth, height: AppSizes.welcomeButtonHeight, alignment: .center)
            }
            
            Text(title)
                .font(AppFonts.textFont(weight: .medium, size: .size16))
                .foregroundColor(AppColors.getColor(.primeryBlack))
        }
        .frame(width: AppSizes.width100, height: AppSizes.height77)
    }
}

//struct WelcomeButtonView_Previews: PreviewProvider {
//    static var previews: some View {
//        WelcomeButtonView()
//    }
//}
