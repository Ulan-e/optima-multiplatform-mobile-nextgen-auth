//
//  PasswirdValidationPointView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import SwiftUI

public struct PasswordValidationPointView: View {
    @ObservedObject var passwordState: PasswordState
    
    public init(passwordState: PasswordState) {
        self.passwordState = passwordState
    }
    
    public var body: some View {
        VStack(alignment: .leading) {
            SubTitleH4MediumView(title: "Пароль должен содержать:", color: AppColors.getColor(.primaryGrayDark))
                .padding(.top, AppSizes.paddingTop32)
            
            PasswordPointView(title: "Заглавные буквы", color: passwordState.colorUpperCase)
                .padding(.top, AppSizes.paddingTop16)
            
            PasswordPointView(title: "Цифры", color: passwordState.colorDigits)
                .padding(.top, AppSizes.paddingTop2)
            
            PasswordPointView(title: "Минимум 8 символов", color: passwordState.colorPasswordCount)
                .padding(.top, AppSizes.paddingTop2)
            
            PasswordPointView(title: "Пароль совпадает", color: passwordState.colorValidPassword)
                .padding(.top, AppSizes.paddingTop2)
        }
    }
}

public struct PasswordPointView: View {
    
    private let title: String
    private let color: Color
    
    public init(title: String, color: Color = AppColors.getColor(.primaryGrayDark)) {
        self.title = title
        self.color = color
    }
    
    public var body: some View {
        HStack(alignment: .center, spacing: AppSizes.spacing5) {
            TitleView(title: "●", color: color)
            DescriptionH5MediumView(title: title, color: AppColors.getColor(.primaryGrayDark))
        }
    }
}

//struct PasswirdValidationPointView_Previews: PreviewProvider {
//    static var previews: some View {
//        PasswirdValidationPointView()
//    }
//}
