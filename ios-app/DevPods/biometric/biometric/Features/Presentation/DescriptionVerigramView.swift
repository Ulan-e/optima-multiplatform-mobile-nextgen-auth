//
//  DescriptionVerigramView.swift
//  biometric
//
//  Created by Kanatbek Torogeldiev on 23/9/22.
//

import SwiftUI
import design

public struct DescriptionVerigramView: View {
    @Environment(\.presentationMode) var presentation
    
    @State var stateButton: GreenButtonState = GreenButtonState(isActive: false)
    @State var showControllQuestion = false
    
    public init() {
    }
    
    public var body: some View {
        VStack (alignment: .leading) {
            TitleView(title: "Подтверждение личности")
                .padding(.top, AppSizes.paddingTop80)
            
            SubTitleH3RegularView(title: "Для прохождении онлайн идентификации необходимо:")
                .padding(.top, AppSizes.paddingTop32)
            
            PharagraphAnimView(title: "Паспорт КР - ID карта\n(серии AN или ID)", image: .idCard, duration: 1.0)
                .padding(.top, AppSizes.paddingTop32)
            
            PharagraphAnimView(title: "Хорошее освещение", image: .sun, duration: 2.0)
            PharagraphAnimView(title: "Нейтральное выражение лица", image: .smile, duration: 3.0)
            PharagraphAnimView(title: "Отсутствие очков или других аксессуаров на лице", image: .glasses, duration: 4.0)
            PharagraphAnimView(title: "Лицо по центру экрана, без движений", image: .frame, duration: 5.0)
            PharagraphAnimView(title: "Волосы собраны и не закрывают лицо, на камере не должно быть других людей", image: .girl, duration: 6.0)
            
            Spacer()
            
            NavigationLink(destination: EmptyView(), isActive: $showControllQuestion) {
                GreenButtonView(title: "Начать") { result in
                    if stateButton.isActive {
                        showControllQuestion = true
                    } else {
                        showControllQuestion = false
                    }
                }
            }
        }
        .onAppear(perform: {
            DispatchQueue.main.asyncAfter(deadline: .now() + 6.2) {
                withAnimation(.easeInOut(duration: 0.3)) {
                    stateButton.isActive = true
                }
            }
        })
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

//struct DescriptionVerigramView_Previews: PreviewProvider {
//    static var previews: some View {
//        DescriptionVerigramView()
//    }
//}
