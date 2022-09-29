//
//  ParagraphTextView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct ParagraphTextView: View {
    private var title: String
    private var subTitle: String
    private var link: String
    private var color: Color
    private var subColor: Color
    private var font: Font
    private let action: ((Bool) -> ())?
    
    public init(title: String,
                subTitle: String,
                link: String,
                color: Color = AppColors.getColor(.primeryBlack),
                subColor: Color = AppColors.getColor(.primaryRed),
                font: Font = AppFonts.textFont(weight: .medium, size: .size14),
                action: ((Bool) -> ())?
    ) {
        self.title = title
        self.subTitle = subTitle
        self.link = link
        self.color = color
        self.subColor = subColor
        self.font = font
        self.action = action
    }
    
    public var body: some View {
//        ForEach(title.split(separator: " "), id: \.self) { line in
//            HStack(spacing: 4) {
//                ForEach(line.split(separator: " "), id: \.self) { part in
//                    self.generateBlock(for: part)
//                }
//            }
//        }
        (
            Text(title)
                .foregroundColor(color)
                .font(font)
            + Text(subTitle)
                .foregroundColor(subColor)
                .font(font)
        )
        .onTapGesture {
            guard let action = action else { return }
            action(false)
        }
    }
    
    private func generateBlock(for str: Substring) -> some View {
        Group {
            if str.starts(with: "~") {
                Text(str.dropFirst().dropLast(1))
                    .font(font)
                    .foregroundColor(color)
                    .onTapGesture { print("tapping ") }
            } else {
                Text(str)
                    .font(font)
                    .foregroundColor(color)
            }
        }
    }
    
}

//struct ParagraphTextView_Previews: PreviewProvider {
//    static var previews: some View {
//        ParagraphTextView()
//    }
//}
