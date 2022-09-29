//
//  Font.swift
//  Optima
//
//  Created by Kanatbek Torogeldiev on 18/9/22.
//

import Foundation
import SwiftUI

public enum FontWeight: String {
    case regular = "TTNormsPro-Regular"
    case bold = "TTNormsPro-Bold"
    case medium = "TTNormsPro-Medium"
}

public enum FontSize: CGFloat {
    case size34 = 34
    case size24 = 24
    case size22 = 22
    case size20 = 20
    case size18 = 18
    case size16 = 16
    case size14 = 14
    case size12 = 12
}

public class AppFonts {
    public static func textFont(weight: FontWeight, size: FontSize) -> Font {
        return Font.custom(weight.rawValue, size: size.rawValue)
    }
}



