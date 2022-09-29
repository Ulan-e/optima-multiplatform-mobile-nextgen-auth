//
//  ContactView.swift
//  common
//
//  Created by Kanatbek Torogeldiev on 26/9/22.
//

import SwiftUI
import design

struct ContactView: View {
    @Environment(\.presentationMode) var presentation
    
    var body: some View {
        VStack(alignment: .leading) {
            PharagraphContactView(image: .whatsapp,
                                  description: "Написать в WhatsApp",
                                  adittional: "+996 (990) 90-59-59")
                .padding(.top, AppSizes.paddingTop32)

            PharagraphContactView(image: .phone,
                                  description: "Короткий номер для абонентов сети Beeline, MegaCom и O! (звонок бесплатный)",
                                  adittional: "9595")
                .padding(.top, AppSizes.paddingTop32)
            
            PharagraphContactView(image: .phoneUser,
                                  description: "Круглосуточная поддержка держателей карточек",
                                  adittional: "+996 (312) 90-59-59")
                .padding(.top, AppSizes.paddingTop32)
            
            PharagraphContactView(image: .phoneRecord,
                                  description: "Бесплатная линия со стационарных телефонов ОАО “Кыргызтелеком”",
                                  adittional: "0-800-800-00-00")
                .padding(.top, AppSizes.paddingTop32)
            Spacer()
        }
        .padding(.horizontal, AppSizes.paddingHorizontal20)
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                NavigationItemBackView { result in
                    self.presentation.wrappedValue.dismiss()
                }
            }
            
            ToolbarItem(placement: .principal) {
                NavigationTitleTextView(title: "Контакты")
            }
        }
    }
}

struct ContactView_Previews: PreviewProvider {
    static var previews: some View {
        ContactView()
    }
}
