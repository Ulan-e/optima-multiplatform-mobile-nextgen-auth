//
//  PhoneNumberView.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct PhoneNumberView: View {
    private let action: ((String) -> ())?
    
    @State var showQuestion = false
    @State var number = ""
    
    @ObservedObject var state = PhoneState()
    
    public init(state: PhoneState,
                action: ((String) -> ())?
    ) {
        self.state = state
        self.action = action
    }
    
    public var body: some View {
        HStack(alignment: .center) {
            SubTitleH4MediumView(title: "+996", color: AppColors.getColor(.primeryBlack))
                .padding(.leading, 10)
            
            PhoneNumberTextView(placeholder: "(000) 00 00 00", state: state)
                .padding(.leading, -5)
                .padding(.bottom, 2)
                .onChange(of: state.phoneNumber) { newValue in
                    guard let action = action else { return }
                    action(newValue)
                }
        }
        .padding()
        .frame(maxWidth: .infinity, minHeight: AppSizes.height50, maxHeight: AppSizes.height50, alignment: .center)
        .background(AppColors.getColor(.primaryGrayDark, opacity: OpacityColor.opacity08))
        .cornerRadius(AppSizes.corner10)
    }
    
}

//struct PhoneNumberView_Previews: PreviewProvider {
//    static var previews: some View {
//        PhoneNumberView()
//    }
//}
