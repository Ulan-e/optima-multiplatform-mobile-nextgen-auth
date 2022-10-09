package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.common.presentation.SmsCodeIntent
import kg.optima.mobile.registration.domain.usecase.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.domain.usecase.CheckSmsCodeUseCase
import org.koin.core.component.inject

class RegistrationSmsCodeIntent(
	override val uiState: RegistrationSmsCodeState,
) : SmsCodeIntent(uiState) {

	private val checkSmsCodeUseCase: CheckSmsCodeUseCase by inject()
	private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()

	private var timeLeft = 0L

	fun smsCodeEntered(
		phoneNumber: String,
		smsCode: String,
		referenceId: String,
	) {
		launchOperation {
			checkSmsCodeUseCase.execute(
				CheckSmsCodeUseCase.Params(
					phoneNumber = phoneNumber,
					verificationCode = smsCode,
					referenceId = referenceId
				)
			).map { RegistrationCheckSmsCodeInfo.OtpCheck(it.success) }
		}
	}

	fun smsCodeRequest(phoneNumber: String, currentTime: Long) {
		launchOperation {
			checkPhoneNumberUseCase.execute(phoneNumber).map {
				timeLeft = it.timeLeft
				startTimer(timeLeft, currentTime)
				RegistrationCheckSmsCodeInfo.Check(it.success)
			}
		}
	}
}