//
//  OtpBoxView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct OtpBoxView: View {
    
    private let number: String
    private var otpMode: OtpMode
    
    public init(number: String, otpMode: OtpMode = .empty) {
        self.number = number
        self.otpMode = otpMode
    }
    
    public var body: some View {
        switch otpMode {
            case .empty:
                EmptyOtpView()
            case .filled:
                FilledOtpView(number: number)
            case .error:
                ErrorOtpView(number: number)
            case .focused:
                FocusedOtpView()
        }
    }
}

//struct OtpBoxView_Previews: PreviewProvider {
//    static var previews: some View {
//        OtpBoxView()
//    }
//}
