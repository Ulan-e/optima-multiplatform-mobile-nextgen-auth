package kg.optima.mobile.registration.data.component

import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModel
import kg.optima.mobile.storage.StorageRepository

class RegistrationPreferencesImpl(
    private val storageRepository: StorageRepository,
) : RegistrationPreferences {

    override var otpTriesModel: OtpTriesModel
        get() = storageRepository.getObject(
            OtpTriesModel.serializer(),
            RegistrationPreferences.OTP_TRIES
        ) ?: OtpTriesModel.NO_TRIES
        set(value) = storageRepository.putObject(
            value,
            OtpTriesModel.serializer(),
            RegistrationPreferences.OTP_TRIES
        )

    override fun clearOtpTries() {
        otpTriesModel = OtpTriesModel.NO_TRIES
    }

}