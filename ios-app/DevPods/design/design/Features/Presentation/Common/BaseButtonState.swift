//
//  BaseButtonState.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

public class BaseButtonState: ObservableObject {
    
    @Published var isActive: Bool
    
    public init(_ active: Bool = false) {
        self.isActive = active
    }
}
