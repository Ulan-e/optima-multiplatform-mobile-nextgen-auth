//
//  ControllQuestionView.swift
//  registrationSecondPart
//
//  Created by Kanatbek Torogeldiev on 24/9/22.
//
import SwiftUI
import design

public struct ControllQuestionView: View {
    @Environment(\.presentationMode) var presentation
    @StateObject var stateButton = GreenButtonState(isActive: true)
    @State var phonePassword = false
    @State var question = "Контрольный вопрос"
    @Namespace var otp
    
    public init() {
    }
    
    public var body: some View {
        VStack(alignment: .leading) {
            
            Spacer()
            
            TitleView(title: "Выберите контрольный вопрос")
            
            DropDownView(list: QuestionList.shared.list) { result in
                question = result
                stateButton.isActive = true
            }
            .padding(.top, AppSizes.paddingTop32)
            
            DescriptionH6RegularView(title: "Контрольный вопрос необходим для подтверждения личности",
                                     color: AppColors.getColor(.primaryGrayDark))
            
            CustomTextView(palceholder: "Ответ") { result in
                checkButtonState(newValue: result, question: question)
            }
                
            Spacer()
            
            NavigationLink(destination: CreatePasswordView(passwordState: PasswordState()),
                           isActive: $phonePassword) {
                GreenButtonView(title: "Продолжить") { result in
                    phonePassword = true
                }
            }
        }
        .environmentObject(stateButton)
        .padding(.horizontal, AppSizes.paddingHorizontal20)
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .principal) {
                NavigationItemView()
            }
        }
        
    }
    
    func checkButtonState(newValue: String, question: String) {
        if question != "Контрольный вопрос" {
            stateButton.isActive = newValue.count <= 0 ? false : true
            if newValue.count == 0 {
                stateButton.isActive = false
            }
        } else {
            stateButton.isActive = false
        }
    }
}

//struct ControllQuestionView_Previews: PreviewProvider {
//    static var previews: some View {
//        ControllQuestionView()
//    }
//}
