//
//  PhoneState.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 21/9/22.
//

import SwiftUI
import Combine

public class PhoneState: ObservableObject {
    private var toCancel: AnyCancellable!
    private var result: String = ""
    
    @Published public var phoneNumber: String = ""
    @Published public var isActive: Bool = false
    
    public init() {
        toCancel = $phoneNumber
            .filter { !self.isFormatValid($0) }
            .map { self.convert($0) }
            .receive(on: RunLoop.main)
            .assign(to: \.phoneNumber, on: self)
    }

    private func isFormatValid(_ str: String) -> Bool {
        switch str.count {
            case 1, 5, 9, 12: return false
            case let x where x > 14 : return false
            default: return true
        }
    }

    private func convert(_ str: String) -> String {
        if str.count <= 14 {
            result = ""
            
            if str.last == " " || str.last == ")" {
                result = String(str.dropLast())
                if str.count == 5 {
                    result = ""
                }
            }
            if str.count == 1 {
                result.append("(")
                result.append(str)
            } else if str.count == 5 && Array(str)[4] != ")" {
                result.append(str)
                insertSymvol(index: str.index(str.startIndex, offsetBy: 4), ch: ")")
                insertSymvol(index: str.index(str.startIndex, offsetBy: 5), ch: " ")
               
            } else if str.count == 9 && Array(str)[8] != " " {
                result.append(str)
                insertSymvol(index: str.index(str.startIndex, offsetBy: 8), ch: " ")
            } else if str.count == 12 && Array(str)[11] != " " {
                result.append(str)
                insertSymvol(index: str.index(str.startIndex, offsetBy: 11), ch: " ")
            }
        } else { result = String(str.dropLast()) }
        
        return result
    }
    
    deinit {
        toCancel.cancel()
    }
    
    private func insertSymvol(index: String.Index, ch: Character){
        result.insert(ch, at: index)
    }
}
