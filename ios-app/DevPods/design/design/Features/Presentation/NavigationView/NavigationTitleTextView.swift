//
//  NavigationTitleTextView.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 26/9/22.
//

import SwiftUI

public struct NavigationTitleTextView: View {
    private let title: String
    
    public init(title: String) {
        self.title = title
    }
    
    public var body: some View {
        SubTitleH3MediumView(title: title)
    }
}

//struct NavigationTitleTextView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavigationTitleTextView()
//    }
//}
