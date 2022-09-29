//
//  OtpBoxState.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI
import Combine

public enum OtpMode {
    case empty
    case filled
    case error
    case focused
}

public class OtpBoxState: ObservableObject {
    @Published public  var success: Bool = true
    @Published public var showingSheet: Bool = false
    @Published public var otpType: OtpMode = .empty
    @Published public var number: String = "552125743"
    @Published public var otpField = "" {
        didSet {
            guard otpField.count <= 4, otpField.last?.isNumber ?? true else {
                otpField = oldValue
                return
            }
        }
    }
    
    public var otp1: String {
        guard otpField.count >= 1 else {
            return ""
        }
        return String(Array(otpField)[0])
    }
    public var otp2: String {
        guard otpField.count >= 2 else {
            return ""
        }
        return String(Array(otpField)[1])
    }
    public var otp3: String {
        guard otpField.count >= 3 else {
            return ""
        }
        return String(Array(otpField)[2])
    }
    public var otp4: String {
        guard otpField.count >= 4 else {
            return ""
        }
        return String(Array(otpField)[3])
    }
    
    public init() {
        
    }
    
}
