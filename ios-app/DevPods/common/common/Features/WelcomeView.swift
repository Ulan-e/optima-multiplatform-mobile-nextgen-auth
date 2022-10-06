//
//  WelcomView.swift
//  biometric
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI
import design
import registration
import biometric
import MultiPlatformLibrary

public struct WelcomeView: View {
    
    @StateObject var stateRedButton = RedButtonState(isActive: true)
    @StateObject var stateGreenButton = GreenButtonState(isActive: true)
    @State var showLogin: Bool = false
    @State var showContact: Bool = false
    @State var showRegistration: Bool = false
    @State var isLoad: Bool = false

    var state: WelcomeState
    var intent: WelcomeIntent

    public init(isLoad: Bool = false) {
        self.isLoad = isLoad

        self.state = WelcomeState()
        self.intent = WelcomeIntent(mppState: state)
    }
    
    public var body: some View {
//        LoadingView(isShowing: isLoad) {
            ZStack {
                VStack {
                    Spacer()
                    
                    TitleView(title: "Добро пожаловать!", size: .size24)
                    
                    DescriptionH4RegularView(title: "Весь банк в одном приложении")
                        .padding(.top, AppSizes.paddingTop32)
                    
                    Spacer()
                    
                    HStack(spacing: AppSizes.spacing50) {
                        WelcomeButtonView(title: "На карте",
                                          image: AppImages.getImage(.map)) { result in
                        }
                        
                        WelcomeButtonView(title: "Языки",
                                          image: AppImages.getImage(.lang)) { result in
                        }
                    }
                    
                    HStack(spacing: AppSizes.spacing50) {
                        WelcomeButtonView(title: "Курсы валют",
                                          image: AppImages.getImage(.rate)) { result in
                        }
                        
                        NavigationLink(destination: ContactView(), isActive: $showContact) {
                            WelcomeButtonView(title: "Контакты",
                                              image: AppImages.getImage(.contact)
                            ) { result in
                                showContact = true
                            }
                        }
                        
                    }.padding(.top, AppSizes.paddingTop40)
                    
                    Spacer()
                    
                    NavigationLink(destination: DescriptionVerigramView(), isActive: $showLogin) {
                        RedButtonView(title: "Войти") { result in
//                            showLogin.toggle()
                            intent.login()
                        }
                    }
                    
                    NavigationLink(destination: OnboardingView(), isActive: $showRegistration) {
                        CustomButtonView(title: "Зарегистрироваться", color: AppColors.getColor(.primaryRed)) { result in
//                            showRegistration = true
                            intent.register()
                        }
                    }
                    
                    DescriptionH6RegularView(title: "Версия  3.7.27", color: AppColors.getColor(.primaryGrayDark))
                        .padding(.bottom, AppSizes.paddingBottom20)
                    
                }
            }
            .padding(.horizontal, AppSizes.paddingHorizontal20)
            .environmentObject(stateRedButton)
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    NavigationItemView()
                }
            }
            .onAppear() {
                observeState()
            }
//        }
    }
    
    private func observeState() {
        state.commonStateFlow.watch { state in
            switch(state) {
            case let x where x is BaseMppStateStateModelInitial:
                print("Init")
            case let x where x is BaseMppStateStateModelLoading:
                print("Loading")
            case let x where x is BaseMppStateStateModelError:
                print("Error")
            case let x where x is WelcomeStateModelNavigateToLogin:
                print("Login")
            case let x where x is WelcomeStateModelNavigateToRegisterAgreement:
                print("Register")
            case .none:
                print("none")
            case .some(let a):
                print("some\(a)")
            default:
                print("Default")
            }
//            if state is BaseMppStateStateModelInitial {
//                print("Init")
//            } else if state is BaseMppStateStateModelLoading {
//                print("Loading")
//                } else if state is BaseMppStateStateModelError {
//                    print("Error")
//                }
//            else {
//                print(state)
//            }

        }
    }
}

//struct WelcomView_Previews: PreviewProvider {
//    static var previews: some View {
//        WelcomeView(factory: SharedFactory)
//    }
//}
