//
//  ValidationPassword.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import Foundation
import SwiftUI

public enum PasswordEnum {
    case upperCase
    case digits
    case count
    case contain
}

public class PasswordState: ObservableObject {
    @Published public var password: String = "" {
        didSet {
            self.setColorValidation()
        }
    }
    @Published public var confirmPassword: String = "" {
        didSet {
            self.setColorconfirmationValidate()
        }
    }
    
    @Published public var showPassword: Bool = false
    @Published public var showConfirmationPassword: Bool = false
    @Published public var passwordStrokeColor: Color = .clear
    @Published public var colorUpperCase: Color = AppColors.getColor(.primaryGrayDark)
    @Published public var colorDigits: Color = AppColors.getColor(.primaryGrayDark)
    @Published public var colorPasswordCount: Color = AppColors.getColor(.primaryGrayDark)
    @Published public var colorValidPassword: Color = AppColors.getColor(.primaryGrayDark)
    @Published public var validPasswordTextFieldStrokeColor: Color = .clear
    
    @ObservedObject public var buttonState: GreenButtonState
    
    public init(buttonState: GreenButtonState = .init(isActive: false)) {
        self.buttonState = buttonState
    }
    
    public func setColorValidation() {
        switch validateUppercase() {
            case true:
                colorUpperCase = AppColors.getColor(.secondaryLightGreen)
            case nil:
                colorUpperCase = AppColors.getColor(.primaryGrayDark)
            default:
                colorUpperCase = AppColors.getColor(.primaryRed)
        }
        
        switch validateDigits() {
            case true:
                colorDigits = AppColors.getColor(.secondaryLightGreen)
            case nil:
                colorDigits = AppColors.getColor(.primaryGrayDark)
            default:
                colorDigits = AppColors.getColor(.primaryRed)
        }
        
        switch validateMinCount() {
            case true:
                colorPasswordCount = AppColors.getColor(.secondaryLightGreen)
            case nil:
                colorPasswordCount = AppColors.getColor(.primaryGrayDark)
            default:
                colorPasswordCount = AppColors.getColor(.primaryRed)
        }
        
        if validateMinCount() ?? false && validateDigits() ?? false && validateUppercase() ?? false {
            passwordStrokeColor = AppColors.getColor(.secondaryGreen)
        } else {
            passwordStrokeColor = .clear
        }
    }
    
    public func setColorconfirmationValidate() {
        switch validateContain() {
        case true:
            buttonState.isActive = true
            colorValidPassword = AppColors.getColor(.secondaryLightGreen)
            validPasswordTextFieldStrokeColor = AppColors.getColor(.secondaryGreen)
        case nil:
            buttonState.isActive = false
            colorValidPassword = AppColors.getColor(.primaryGrayDark)
            validPasswordTextFieldStrokeColor = .clear
        default:
            buttonState.isActive = false
            validPasswordTextFieldStrokeColor = AppColors.getColor(.primaryRed)
            colorValidPassword = AppColors.getColor(.primaryRed)
            
        }
    }
    
    
    public func validateUppercase() -> Bool? {
        guard !password.isEmpty else { return nil }
        var upperExist = false
        
        for letter in password {
            if letter.isUppercase {
                upperExist = true
            }
        }
        
        return upperExist
    }
    
    public func validateDigits() -> Bool? {
        guard !password.isEmpty else { return nil }
        var digtsExist = false
        
        for number in self.password {
            if number.isNumber {
                digtsExist = true
            }
        }
        
        return digtsExist
    }
    
    public func validateMinCount() -> Bool? {
        guard !password.isEmpty else { return nil }
        var isGreater = false
        
        if self.password.count >= 8 {
            isGreater = true
        }
        
        return isGreater
    }
    
    public func validateContain() -> Bool? {
        guard !confirmPassword.isEmpty else { return nil }
        var isContain = false

        if confirmPassword == password {
            isContain = true
        }
        
        return isContain
    }
}
