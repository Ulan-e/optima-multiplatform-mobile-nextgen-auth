//
//  PasswordView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import SwiftUI

public struct PasswordView: View {
    
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
                if passwordState.showPassword {
                    TextField(placeholder,
                              text: $passwordState.password)
                    .disableAutocorrection(true)
                } else {
                    SecureField(placeholder,
                                text: $passwordState.password)
                    .disableAutocorrection(true)
                }
                
                Button(action: { passwordState.showPassword.toggle()}) {
                    AppImages.getImage(passwordState.showPassword ? .eyeOn : .eyeOff)
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
            .overlay(RoundedRectangle(cornerRadius: AppSizes.standartCorner).stroke(passwordState.passwordStrokeColor))
            .onChange(of: passwordState.password) { newValue in
                
            }
        }
    }
}

//struct PasswordView_Previews: PreviewProvider {
//    static var previews: some View {
//        PasswordView()
//    }
//}
