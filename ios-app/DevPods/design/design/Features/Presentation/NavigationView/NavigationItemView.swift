//
//  NavigationItemView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI

public struct NavigationItemView: View {
    
    public init() {
        
    }
    
    public var body: some View {
        AppImages.getImage(.logoApp)
            .frame(width: AppSizes.width124, height: AppSizes.height34)
    }
}

struct NavigationItemView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationItemView()
    }
}
