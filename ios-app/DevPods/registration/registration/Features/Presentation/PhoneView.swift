//
//  PhoneView.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI
import design
import otp

struct PhoneView: View {
    @Environment(\.presentationMode) var presentation
    @StateObject var stateButton = GreenButtonState()
    @ObservedObject var statePhone = PhoneState()
    @State var showOtp = false
    
    init() {
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            
            TitleView(title: "Введите номер телефона:")
                .padding(.top, AppSizes.paddingTop80)
            
            DescriptionH5MediumView(title: "Номер телефона будет использоваться для переводов и отправки SMS-кода для подтверждения некоторых операций")
                .padding(.top, AppSizes.paddingTop32)
            
            PhoneNumberView(state: statePhone) { result in
                checkButtonState(newValue: result)
            }
            
            Spacer()
            
            NavigationLink(destination: OtpView(), isActive: $showOtp) {
                GreenButtonView(title: "Продолжить") { result in
                    showOtp = true
                }
            }
            
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
    
    private func checkButtonState(newValue: String) {
        print("newValue = \(newValue)")
        switch newValue.count {
            case 14: stateButton.isActive = true
            default : stateButton.isActive = false
        }
    }
}

struct PhoneView_Previews: PreviewProvider {
    static var previews: some View {
        PhoneView()
    }
}
