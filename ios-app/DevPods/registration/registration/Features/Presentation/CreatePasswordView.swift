//
//  CreatePasswordView.swift
//  registrationSecondPart
//
//  Created by Kanatbek Torogeldiev on 24/9/22.
//

import SwiftUI
import design

public struct CreatePasswordView: View {
    @Environment(\.presentationMode) var presentation
    @ObservedObject var passwordState: PasswordState
    
    @State var successRegistered = false
    @State var state: GreenButtonState = GreenButtonState(isActive: false)
    @State var stateGreenButton: GreenButtonState = GreenButtonState(isActive: false)
    
    public init(passwordState: PasswordState) {
        self.passwordState = passwordState
        self.passwordState.buttonState = state
    }
    
    public var body: some View {
        VStack(alignment: .leading) {
            
            Spacer()
            TitleView(title: "Создание пароля")
            
            PasswordView(placeholder: "Пароль", passwordState: passwordState)
                .padding(.top, AppSizes.paddingTop32)
            
            DescriptionH6RegularView(title: "Пароль для входа в приложение",
                                     color: AppColors.getColor(.primaryGrayDark))
            .padding(.top, AppSizes.paddingTop8)
            
            ConfirmPasswordView(placeholder: "Повторите пароль", passwordState: passwordState)
                .padding(.top, AppSizes.paddingTop24)
            
            PasswordValidationPointView(passwordState: passwordState)
            
            Spacer()
            
//            NavigationLink(destination: EmptyView(), isActive: $successRegistered) {
                GreenButtonView(title: "Продолжить") { result in
                    if state.isActive {
                        successRegistered = true
                    } else {
                        successRegistered = false
                    }
                }
//            }
            
                .bottomSheet(isPresented: $successRegistered,
                             height: 380,
                             topBarHeight: 16,
                             topBarCornerRadius: 16,
                             showTopIndicator: true
                ) {
        //                    RedBottomSheetView(title: "Что-то пошло не так",
        //                                       description: "Решите свой вопрос через нашу заботливую поддержку прямо сейчас",
        //                                       positiveButtonTitle: "Связаться с банком",
        //                                       neagativeButtonTitle: "Отмена") { result in
        //                        print("tapped Связаться с банком")
        //                    } negativeAction: { result in
        //                        print("tapped Отмена")
        //                    }
                    
                    GreenBottomSheetView(title: "Поздравляем!\nВы зарегистрированы в Optima24",
                                         subTitle: "Ваш Client ID",
                                         clientId: "123456",
                                         description: "Запомните его. Он является  вашим логином для входа\nв “Optima 24”. ",
                                         positiveButtonTitle: "Готово",
                                         neagativeButtonTitle: ""
                    ) { result in

                    } negativeAction: { result in
                        
                    }
                }
                .ignoresSafeArea(.container)
                .environmentObject(stateGreenButton)
        }
        .environmentObject(state)
        .padding(.horizontal, AppSizes.paddingHorizontal20)
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                NavigationItemBackView { result in
                    self.presentation.wrappedValue.dismiss()
                }
            }
            ToolbarItem(placement: .principal) { NavigationItemView() }
        }
    }
}

//struct CreatePasswordView_Previews: PreviewProvider {
//    static var previews: some View {
//        CreatePasswordView()
//    }
//}
