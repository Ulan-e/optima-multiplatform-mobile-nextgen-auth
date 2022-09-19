package kg.optima.mobile.registration.data.component

import kg.optima.mobile.registration.data.model.otp_tries.OtpTryModel

interface RegistrationPreferences {

    companion object {
        const val OTP_TRIES = "OTP_TRIES"
    }

    var otpTryModel : OtpTryModel

    fun clearOtpTries()
}