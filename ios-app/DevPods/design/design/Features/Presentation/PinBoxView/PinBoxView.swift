//
//  PinBoxView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

import SwiftUI

public struct PinBoxView: View {
    
    @StateObject var state = PinBoxState()
    @Binding var pincode : String
    var index : Int
    
    public var body: some View {
        ZStack {
            Text("")
//            switch state {
//                case .empty:
//                    RoundedRectangle(cornerRadius: AppSizes.standartCorner, style: .continuous)
//                        .frame(width: AppSizes.pinBoxWidth, height: AppSizes.pinBoxHeight)
//                        .background(AppColors.getColor(.primaryGrayLight))
//
//                case .error(let n):
//                    RoundedRectangle(cornerRadius: AppSizes.standartCorner, style: .continuous)
//                        .stroke(AppColors.getColor(.primaryRed))
//                        .frame(width: AppSizes.pinBoxWidth, height: AppSizes.pinBoxHeight)
//                        .background(AppColors.getColor(.primaryGrayLight))
//
//                    Text(pincode)
//                        .foregroundColor(AppColors.getColor(.primeryBlack))
//                        .font(AppFonts.textFont(weight: .medium, size: .size24))
//
//                default:
//                    RoundedRectangle(cornerRadius: AppSizes.standartCorner, style: .continuous)
//                        .fill(AppColors.getColor(.primaryWhite))
//                        .frame(width: AppSizes.pinBoxWidth, height: AppSizes.pinBoxHeight)
//                        .background(AppColors.getColor(.primaryGrayLight))
//            }
            
            
//            ZStack {
//
//                Text(pincode)
//                    .foregroundColor(AppColors.getColor(.primeryBlack))
//                    .font(AppFonts.textFont(weight: .medium, size: .size24))
//
//
//            }
            
           
            
        }
    }
}

//struct PinBoxView_Previews: PreviewProvider {
//    static var previews: some View {
//        PinBoxView()
//    }
//}
