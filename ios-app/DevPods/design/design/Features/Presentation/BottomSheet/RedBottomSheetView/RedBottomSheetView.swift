//
//  RedBottomSheetView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 25/9/22.
//

import SwiftUI

public struct RedBottomSheetView: View {
    
    private let title: String
    private let description: String
    private let positiveButtonTitle: String
    private let neagativeButtonTitle: String
    private let positiveAction: ((AnyObject) -> ())?
    private let negativeAction: ((AnyObject) -> ())?
    @StateObject var stateRedButton = RedButtonState(isActive: true)
    
    public init(title: String,
                description: String,
                positiveButtonTitle: String,
                neagativeButtonTitle: String,
                positiveAction: ((AnyObject) -> ())?,
                negativeAction: ((AnyObject) -> ())?
    
    ) {
        self.title = title
        self.description = description
        self.positiveButtonTitle = positiveButtonTitle
        self.neagativeButtonTitle = neagativeButtonTitle
        self.positiveAction = positiveAction
        self.negativeAction = negativeAction
    }
    
    public var body: some View {
        VStack(alignment: .center) {
            if !title.isEmpty {
                TitleView(title: title)
                    .lineLimit(10)
                    .multilineTextAlignment(.center)
                    .padding(.top, AppSizes.paddingTop32)
            }
            
            if !description.isEmpty {
                DescriptionH4RegularView(title: description)
                    .padding(.top, AppSizes.paddingTop8)
            }
            
            if !positiveButtonTitle.isEmpty {
                Button {
                    guard let positiveAction = positiveAction else { return }
                    positiveAction(true as AnyObject)
                } label: {
                    RedButtonView(title: positiveButtonTitle) { result in
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
        .environmentObject(stateRedButton)
        .padding(.horizontal, AppSizes.paddingHorizontal20)
    }
}
//
//struct RedBottomSheetView_Previews: PreviewProvider {
//    static var previews: some View {
//        RedBottomSheetView()
//    }
//}
