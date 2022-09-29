
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI
import design
import biometric

public struct OtpView: View {
    @Environment(\.presentationMode) var presentation
    
    @StateObject var otpState = OtpBoxState()
    @StateObject var stateButton = GreenButtonState(isActive: true)
    @State var showControllQuestion: Bool = false
    
    
    public init(showControllQuestion: Bool = false) {
        self.showControllQuestion = showControllQuestion
    }
    
    public var body: some View {
        VStack(alignment: .leading) {
            TitleView(title: "Введите код подтверждения")
                .padding(.top, AppSizes.paddingTop80)
            
            DescriptionH5MediumView(title: "Вводя код из SMS, вы подписываете оферту, подтверждая свое согласие. Мы отправили СМС код на номер:",
                                    color: AppColors.getColor(.primaryGrayDark))
                .padding(.top, AppSizes.paddingTop8)
            
            DescriptionH5MediumView(title: "+996 (556) 77 56 44",
                                    color: AppColors.getColor(.primeryBlack))
            
            HStack(alignment: .center) {
                ZStack {
                    HStack (spacing: AppSizes.paddingHorizontal20) {
                        OtpBoxView(number: otpState.otp1,
                                   otpMode: (!otpState.success && otpState.otpField.count == 4) ? .error : otpState.otpField.count == 0 ? .focused : .filled)
                        
                        OtpBoxView(number: otpState.otp2,
                                   otpMode: (!otpState.success && otpState.otpField.count == 4) ? .error : otpState.otpField.count == 1 ? .focused : (otpState.otpField.count == 2 || otpState.otpField.count == 3 || otpState.otpField.count == 4 ) ? .filled : .empty)
                        
                        OtpBoxView(number: otpState.otp3,
                                   otpMode: (!otpState.success && otpState.otpField.count == 4) ? .error : otpState.otpField.count == 2 ? .focused : (otpState.otpField.count == 3 || otpState.otpField.count == 4 ) ? .filled : .empty)
                        
                        OtpBoxView(number: otpState.otp4,
                                   otpMode: (!otpState.success && otpState.otpField.count == 4) ? .error : otpState.otpField.count == 3 ? .focused : otpState.otpField.count == 4 ? .filled : .empty)
                    }
                    
                    TextField("", text: $otpState.otpField)
                        .frame(width: AppSizes.otpBoxWidth * 3, height: AppSizes.otpBoxWidth)
                        .textContentType(.oneTimeCode)
                        .foregroundColor(.clear)
                        .font(AppFonts.textFont(weight: .medium, size: .size24))
                        .accentColor(.clear)
                        .background(Color.clear)
                        .keyboardType(.numberPad)
                        .onChange(of: otpState.otpField) { newValue in
                            print("newValue: \(newValue)")
                            if newValue.count == 1 {
                                otpState.success = true
                            }
                            
                            if newValue.count == 4 {
                                if newValue != "2222" {
                                    otpState.success = false
//                                    withAnimation(.default) {
//                                       self.otpState.attempts += 1
//                                    }
                                    
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                        self.otpState.otpField = ""
//                                        self.otpState.attemptCount += 1
//                                        if self.otpState.attemptCount == 3 {
                                            self.otpState.showingSheet = true
//                                        }
                                    }
                                }
                            }
                        }
                }
//                .modifier(Shake(animatableData: CGFloat(otpModel.attempts)))
            }
            .padding(.top, AppSizes.paddingTop32)
            .frame(alignment: .center)

            Spacer()
            
            NavigationLink(destination: DescriptionVerigramView(), isActive: $showControllQuestion) {
                GreenButtonView(title: "Продолжить") { result in
                    if stateButton.isActive {
                        showControllQuestion = true
                    } else {
                        showControllQuestion = false
                    }
                }
            }
            .padding(.bottom, AppSizes.paddingBottom20)
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

//struct OtpView_Previews: PreviewProvider {
//    static var previews: some View {
//        OtpView()
//    }
//}
