//
//  DropDownView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import SwiftUI

public struct DropDownView: View {
    
    @State private var isExpanded: Bool
    @State private var selected: String
    private var list: [String]
    
    private var action: (String) -> ()
    
    public init(isExpanded: Bool = false,
                selected: String = "Контрольный вопрос",
                list: [String],
                action: @escaping (String) -> Void
    ) {
        self.isExpanded = isExpanded
        self.selected = selected
        self.list = list
        self.action = action
    }
    
    public var body: some View {
        DisclosureGroup("\(selected)", isExpanded: $isExpanded) {
            ScrollView {
                VStack(alignment: .leading, spacing: AppSizes.spacing5) {
                    ForEach(list, id: \.self) { item in
                        Text("\(item)")
                            .frame(alignment: .leading)
                            .multilineTextAlignment(.leading)
                            .lineLimit(4)
                            .padding([.top, .bottom], AppSizes.paddingTop8)
                            .onTapGesture {
                                self.selected = item
                                self.action(item)
                                withAnimation{
                                    self.isExpanded.toggle()
                                }
                            }

                        Divider()
                    }
                    .offset(x: -16.0, y: 0)
                }
                .padding()
            }
        }
        .padding(.all)
        .accentColor(AppColors.getColor(.primeryBlack))
        .foregroundColor(AppColors.getColor(.primeryBlack))
        .background(AppColors.getColor(.primaryGrayDark, opacity: OpacityColor.opacity08))
        .cornerRadius(AppSizes.standartCorner)
        .multilineTextAlignment(.leading)
        .frame(alignment: .leading)
    }
}

//struct DropDownView_Previews: PreviewProvider {
//    static var previews: some View {
//        DropDownView()
//    }
//}
