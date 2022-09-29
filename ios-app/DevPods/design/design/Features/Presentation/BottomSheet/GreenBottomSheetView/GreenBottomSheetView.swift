//
//  GreenBottomSheetView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 25/9/22.
//

import SwiftUI

public struct GreenBottomSheetView: View {
    
    private let title: String
    private let subTitle: String
    private let clientId: String
    private let description: String
    private let positiveButtonTitle: String
    private let neagativeButtonTitle: String
    private let positiveAction: ((AnyObject) -> ())?
    private let negativeAction: ((AnyObject) -> ())?
    
    @StateObject var stateGreenButton = GreenButtonState(isActive: true)
    
    public init(title: String,
                subTitle: String,
                clientId: String,
                description: String,
                positiveButtonTitle: String,
                neagativeButtonTitle: String,
                positiveAction: ( (AnyObject) -> Void)?,
                negativeAction: ( (AnyObject) -> Void)?
    ) {
        self.title = title
        self.subTitle = subTitle
        self.clientId = clientId
        self.description = description
        self.positiveButtonTitle = positiveButtonTitle
        self.neagativeButtonTitle = neagativeButtonTitle
        self.positiveAction = positiveAction
        self.negativeAction = negativeAction
    }
    
    
    public var body: some View {
        VStack(alignment: .center, spacing: 0) {
            if !title.isEmpty {
                TitleView(title: title)
                    .lineLimit(10)
                    .multilineTextAlignment(.center)
                    .padding(.top, AppSizes.paddingTop32)
                
            }
            
            if !subTitle.isEmpty {
                DescriptionH4MediumView(title: subTitle).padding(.top, AppSizes.paddingTop16)
            }
            
            if !clientId.isEmpty {
                TitleView(title: clientId, size: .size34, color: AppColors.getColor(.secondaryGreen))
                    .padding(.top, AppSizes.paddingTop6)
            }
            
            if !description.isEmpty {
                DescriptionH4RegularView(title: description)
                    .lineLimit(10)
                    .multilineTextAlignment(.center)
                    .padding(.top, AppSizes.paddingTop12)
            }
            
            if !positiveButtonTitle.isEmpty {
                Button {
                    guard let positiveAction = positiveAction else { return }
                    positiveAction(true as AnyObject)
                } label: {
                    GreenButtonView(title: positiveButtonTitle) { result in
                    }
                    .padding(.top, AppSizes.paddingTop32)
                }
            }
            
            if !neagativeButtonTitle.isEmpty {
                Button {
                    guard let negativeAction = negativeAction else { return }
                    negativeAction(true as AnyObject)
                } label: {
                    CustomButtonView(title: neagativeButtonTitle) { result in
                    }
                    .padding(.bottom, AppSizes.paddingBottom20)
                }
            }
        }
        .environmentObject(stateGreenButton)
        .padding(.horizontal, AppSizes.paddingHorizontal20)
    }
}

//struct GreenBottomSheetView_Previews: PreviewProvider {
//    static var previews: some View {
//        GreenBottomSheetView()
//    }
//}
