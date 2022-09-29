//
//  MockDatasClass.swift
//  design
//
//  Created by Kanatbek Torogeldiev on 22/9/22.
//

import Foundation

public struct QuestionList: Hashable {
    public static let shared = QuestionList()
    public let list = ["Какие 5 последних цифр вашей кредитной карты?",
                       "Как звали вашего лучшего друга детства?",
                       "Какое имя вашей бабушки?",
                       "Какая кличка вашего животного?",
                       "Какой ваш любимый фильм?", "Ваш любимый цвет?"]
}
