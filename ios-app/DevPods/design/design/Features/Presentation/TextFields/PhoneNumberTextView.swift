//
//  PhoneNumberTextView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct PhoneNumberTextView: View {
    private let placeholder: String
    
    @ObservedObject private var state: PhoneState
    
    public init(placeholder: String,
                state: PhoneState
    ) {
        self.placeholder = placeholder
        self.state = state
    }
    
    public var body: some View {
        TextField(placeholder, text: $state.phoneNumber)
            .font(AppFonts.textFont(weight: .medium, size: .size16))
            .foregroundColor(AppColors.getColor(.primeryBlack))
            .textContentType(.telephoneNumber)
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
    }
}
