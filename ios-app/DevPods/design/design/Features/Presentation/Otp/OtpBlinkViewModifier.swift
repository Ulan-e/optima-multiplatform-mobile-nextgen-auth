//
//  OtpBlinkViewModifier.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 26/9/22.
//

import SwiftUI

struct OtpBlinkViewModifier: ViewModifier {
    @State private var blinking: Bool = false
    let duration: Double
    
    func body(content: Content) -> some View {
        content
            .opacity(blinking ? 0 : 1)
            .animation(.easeOut(duration: duration).repeatForever())
            .onAppear {
                withAnimation {
                    blinking = true
                }
            }
    }
}


