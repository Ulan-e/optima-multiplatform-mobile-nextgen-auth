package kg.optima.mobile.design_system.android.ui.text_fields

import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants

fun phoneTextFormatter(text: String) : String {

    val trimmed = if (text.length >= Constants.PHONE_NUMBER_LENGTH) {
        text.substring(0 until Constants.PHONE_NUMBER_LENGTH)
    } else {
        text
    }

    val formattedPhoneString = emptyString
    formattedPhoneString.plus(Constants.PHONE_NUMBER_CODE)
        for (i in trimmed.indices) {
            if (i == 0) formattedPhoneString.plus("(")
            formattedPhoneString.plus(trimmed[i])

            if (i == 2) formattedPhoneString.plus(")")
            if (i % 3 == 2 && i != 8) formattedPhoneString.plus(" ")
        }
        val mask = Constants.PHONE_NUMBER_MASK
    formattedPhoneString.plus(mask.takeLast(mask.length - text.length))

    return formattedPhoneString
}