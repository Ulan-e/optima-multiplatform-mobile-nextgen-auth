//
//  Font.swift
//  Optima
//
//  Created by Kanatbek Torogeldiev on 18/9/22.
//

import Foundation
import SwiftUI

public enum AppImage: String {
    case logoApp = "ic_logo_app"
    case checkboxOn = "ic_checkbox_on"
    case checkboxOff = "ic_checkbox_off"
    case radioOn = "ic_radio_on"
    case radioOff = "ic_radio_off"
    case contact = "ic_contact"
    case rate = "ic_rate"
    case map = "ic_map"
    case lang = "ic_lang"
    case arrowLeft = "ic_left"
    case userCheck = "ic_user_check"
    case docCheck = "ic_doc_check"
    case close = "ic_close"
    case upload = "ic_upload"
    case eyeOn = "ic_eye_on"
    case eyeOff = "ic_eye_off"
    case sun = "ic_sun"
    case frame = "ic_frame"
    case girl = "ic_girl_face"
    case glasses = "ic_glasses"
    case idCard = "ic_id_card"
    case smile = "ic_smile"
    case phoneRecord = "ic_phone_record"
    case phoneUser = "ic_phone_user"
    case phone = "ic_phone"
    case whatsapp = "ic_whatsapp"
    
    
    
}

public class AppImages {
    public static func getImage(_ image: AppImage) -> Image {
        return Image(
            image.rawValue,
            bundle: Bundle(for: AppImages.self)
        )
    }
}

