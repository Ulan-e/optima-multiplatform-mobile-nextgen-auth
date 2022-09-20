package kg.optima.mobile.registration.data.component

import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModelList
import kg.optima.mobile.storage.StorageRepository

class RegistrationPreferencesImpl(
	private val storageRepository: StorageRepository,
) : RegistrationPreferences {

	override var otpTriesModelList: OtpTriesModelList
		get() {
			return storageRepository.getObject(
				OtpTriesModelList.serializer(),
				RegistrationPreferences.OTP_TRIES
			) ?: OtpTriesModelList.NO_TRIES
		}
		set(value) = storageRepository.putObject(
			value,
			OtpTriesModelList.serializer(),
			RegistrationPreferences.OTP_TRIES
		)

}