package kg.optima.mobile.android.ui.features.registration.sms_otp

import java.io.Serializable

class RegistrationOtpModel(
	val phoneNumber: String,
	val timeLeft: Long,
	val referenceId: String,
) : Serializable