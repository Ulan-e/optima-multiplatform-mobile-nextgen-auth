//
//  EmptyOtpView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI

public struct EmptyOtpView: View {
    
    public var body: some View {
        Text("")
            .font(.title)
            .frame(width: AppSizes.otpBoxWidth, height: AppSizes.otpBoxWidth)
            .background(
                RoundedRectangle(cornerRadius: AppSizes.standartCorner)
                    .fill(Color.gray.opacity(OpacityColor.opacity10))
            )
    }
}

//struct EmptyOtpView_Previews: PreviewProvider {
//    static var previews: some View {
//        EmptyOtpView()
//    }
//}
