//
//  ConfirmPasswordView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import SwiftUI

public struct ConfirmPasswordView: View {
    var placeholder: String = "Пароль"
    
    @ObservedObject var passwordState: PasswordState
    
    public init(placeholder: String,
                passwordState: PasswordState
    ) {
        self.placeholder = placeholder
        self.passwordState = passwordState
    }
    
    public var body: some View {
        VStack {
            HStack {
                if passwordState.showConfirmationPassword {
                    TextField(placeholder,
                              text: $passwordState.confirmPassword)
                    .disableAutocorrection(true)
                } else {
                    SecureField(placeholder,
                                text: $passwordState.confirmPassword)
                    .disableAutocorrection(true)
                }
                
                Button(action: { passwordState.showConfirmationPassword.toggle()}) {
                    AppImages.getImage(passwordState.showConfirmationPassword ? .eyeOn : .eyeOff)
                }
            }
            .frame(maxWidth: .infinity,
                   minHeight: AppSizes.height50,
                   maxHeight: AppSizes.height50,
                   alignment: .center)
            .padding(.horizontal, AppSizes.paddingHorizontal12)
            .font(AppFonts.textFont(weight: .medium, size: .size14))
            .foregroundColor(AppColors.getColor(.primaryGrayDark))
            .background(AppColors.getColor(.primaryGrayDark, opacity: OpacityColor.opacity08))
            .cornerRadius(AppSizes.standartCorner)
            .overlay(RoundedRectangle(cornerRadius: AppSizes.standartCorner).stroke(passwordState.validPasswordTextFieldStrokeColor))
        }
    }
}

//struct ConfirmPasswordView_Previews: PreviewProvider {
//    static var previews: some View {
//        ConfirmPasswordView()
//    }
//}
