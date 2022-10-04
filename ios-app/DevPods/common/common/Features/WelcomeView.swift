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

    var product: FactoryProduct<WelcomeIntent, WelcomeState>
    var state: WelcomeState?
    
    public init(isLoad: Bool = false) {
        self.isLoad = isLoad
        
        self.product = WelcomeFactory().create()
        self.state = product.state
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
                            showLogin.toggle()
                        }
                    }
                    
                    NavigationLink(destination: OnboardingView(), isActive: $showRegistration) {
                        CustomButtonView(title: "Зарегистрироваться", color: AppColors.getColor(.primaryRed)) { result in
                            showRegistration = true
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
        guard let state = self.state else { return }
        state.commonStateFlow.watch { state in
//            switch(state) {
//            case .none:
//                
//            case .some(_):
//                
//            }
        }
    }
}

//struct WelcomView_Previews: PreviewProvider {
//    static var previews: some View {
//        WelcomeView(factory: SharedFactory)
//    }
//}
