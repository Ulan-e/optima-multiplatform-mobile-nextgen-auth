//
//  NavigationItemBackView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct NavigationItemBackView: View {
    private let action: ((Bool) -> ())?
    
    public init(action: ((Bool) -> ())?) {
        self.action = action
    }
    
    public var body: some View {
        Button {
            guard let action = action else { return }
            action(false)
        } label: {
            AppImages.getImage(.arrowLeft)
                .frame(width: AppSizes.width24, height: AppSizes.height24)
        }
    }
}

//struct NavigationItemBackView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavigationItemBackView()
//    }
//}
