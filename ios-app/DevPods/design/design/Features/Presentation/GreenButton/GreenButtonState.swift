//
//  GreenButtonState.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

public class GreenButtonState: ObservableObject {
    @Published public var isActive: Bool = false
    
    public init(isActive: Bool = false) {
        self.isActive = isActive
    }
}
