//
//  PinBoxState.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 19/9/22.
//

public enum PinBoxStates {
    case empty
    case focused
    case error(Int?)
    case filled(Int)
}

public class PinBoxState: ObservableObject {
    
    @Published var state: PinBoxStates
    
    public init(state: PinBoxStates = .empty) {
        self.state = state
    }
}
