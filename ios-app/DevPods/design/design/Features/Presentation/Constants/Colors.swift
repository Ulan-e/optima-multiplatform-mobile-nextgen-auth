//
//  Font.swift
//  Optima
//
//  Created by Kanatbek Torogeldiev on 18/9/22.
//

import Foundation
import SwiftUI

public enum PrimaryColor: UInt32 {
    case primaryRed = 0xE21D26
    case primaryGray = 0x90929C
    case primaryGrayLight = 0xF0F0F0
    case primaryGrayDark = 0x999BA3
    case primaryWhite = 0xF5F5F5
    case primeryBlack = 0x404040
    
    case secondaryGreen = 0x00A873
    case secondarySystemGreen = 0x34C759
    case secondaryLightGreen = 0x2FBF53
    case secondarySystemBlue = 0x007AFF
    case secondaryOrange = 0xFF9500
    case secondaryLight = 0xF6F6F7
    case secondaryDark = 0x6B6B6C
    case secondaryWhite = 0xFFFFFF
}

public class OpacityColor {
    public static let opacity1: CGFloat = 1.0
    public static let opacity80: CGFloat = 0.8
    public static let opacity60: CGFloat = 0.6
    public static let opacity50: CGFloat = 0.5
    public static let opacity40: CGFloat = 0.4
    public static let opacity30: CGFloat = 0.3
    public static let opacity20: CGFloat = 0.2
    public static let opacity10: CGFloat = 0.1
    public static let opacity08: CGFloat = 0.08
}

public class AppColors {
    public static func getColor(_ color: PrimaryColor,
                                opacity: CGFloat = OpacityColor.opacity1
    ) -> Color {
        return Color(hex: color.rawValue, opacity: opacity)
    }
}

extension Color {
    init(hex: UInt32, opacity: CGFloat = 1.0) {
        if #available(iOS 15.0, *) {
            self.init(
                uiColor: .init(
                    red: CGFloat(0xFF & (hex >> 0x10)) / 0xFF,
                    green: CGFloat(0xFF & (hex >> 0x08)) / 0xFF,
                    blue: CGFloat(0xFF & (hex >> 0x00)) / 0xFF,
                    alpha: opacity
                )
            )
        } else {
            self.init(
                UIColor(
                    red: CGFloat(0xFF & (hex >> 0x10)) / 0xFF,
                    green: CGFloat(0xFF & (hex >> 0x08)) / 0xFF,
                    blue: CGFloat(0xFF & (hex >> 0x00)) / 0xFF,
                    alpha: opacity
                ).cgColor
            )
        }
    }
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        let a, r, g, b: UInt64
        
        Scanner(string: hex).scanHexInt64(&int)
        
        switch hex.count {
            case 3: // RGB (12-bit)
                (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
            case 6: // RGB (24-bit)
                (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
            case 8: // ARGB (32-bit)
                (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
            default:
                (a, r, g, b) = (1, 1, 1, 0)
        }
        self.init(.sRGB,
                  red: Double(r) / 255,
                  green: Double(g) / 255,
                  blue: Double(b) / 255,
                  opacity: Double(a) / 255
        )
    }
}
