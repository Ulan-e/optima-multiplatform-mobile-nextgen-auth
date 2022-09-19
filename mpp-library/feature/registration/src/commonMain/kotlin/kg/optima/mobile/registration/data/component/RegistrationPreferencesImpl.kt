package kg.optima.mobile.registration.data.component

import kg.optima.mobile.registration.data.model.otp_tries.OtpTryModel
import kg.optima.mobile.storage.StorageRepository

class RegistrationPreferencesImpl(
    private val storageRepository: StorageRepository,
) : RegistrationPreferences {

    override var otpTryModel: OtpTryModel
        get() = storageRepository.getObject(
            OtpTryModel.serializer(),
            RegistrationPreferences.OTP_TRIES
        ) ?: OtpTryModel.NO_TRIES
        set(value) = storageRepository.putObject(
            value,
            OtpTryModel.serializer(),
            RegistrationPreferences.OTP_TRIES
        )

    override fun clearOtpTries() {
        otpTryModel = OtpTryModel.NO_TRIES
    }

}