//
//  OnboardingView.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI
import design

public struct OnboardingView: View {
    @State var show = false
    @State var isShare = false
    @State var phoneShow = false
    
    @Environment(\.presentationMode) var presentation
    @StateObject var stateButton = GreenButtonState(isActive: true)
    
    public init(show: Bool = false, isShare: Bool = false) {
        self.show = show
        self.isShare = isShare
    }
    
    public var body: some View {
        VStack(alignment: .leading) {
            
            TitleView(title: "Обратите внимание!")
                .padding(.top, AppSizes.paddingTop80)
            
            OnboardingDestriptionView(title: "Убедитесь, что Вы вляетесь резидентом КР с оригинальным паспортом - ID карта образца 2014 и 2017 года",
                          image: .userCheck)
            .padding(.top, AppSizes.paddingTop32)
            
            OnboardingRedDescriptionView(title: "А также согласны на обработку своих персональных данных, в соответствии с Законом Кыргызской Республики «Об информации персонального характера» для целей получения банковских услуг, и выполнения требований действующего законодательства КР и с ",
                                         subTitle: "условиями оферты", link: "",
                                         image: .docCheck) { result in
                show = true
            }
            
            Spacer()
            
            DescriptionH5RegularView(title: "Нажимая на кнопку “Согласен”,\nподтверждаю, что ознакомлен и согласен с условиями договора")
            
            NavigationLink(destination: PhoneView(), isActive: $phoneShow) {
                GreenButtonView(title: "Согласен") { result in
                    phoneShow = true
                }
            }
            
            
            BlackBorderButtonView(title: "Не согласен") { result in
            }
            .padding(.top, AppSizes.paddingTop16)
        }
        .sheet(isPresented: $show) {
            WebViewContent(urlLink: URL(string: "https://forms.optimabank.kg/offers/o24-20220101-ru.html")!,
                           show: $show,
                           isShare: $isShare)
        }
        .environmentObject(stateButton)
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

//struct OnboardingView_Previews: PreviewProvider {
//    static var previews: some View {
//        OnboardingView()
//    }
//}
