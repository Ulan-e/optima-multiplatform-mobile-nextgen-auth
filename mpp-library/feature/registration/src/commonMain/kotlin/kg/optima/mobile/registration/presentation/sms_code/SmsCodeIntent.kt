package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.domain.CheckSmsCodeUseCase
import kotlinx.coroutines.delay
import org.koin.core.component.inject

class SmsCodeIntent(
	override val state: SmsCodeState,
) : Intent<CheckSmsCodeInfo>() {

	private val checkSmsCodeUseCase: CheckSmsCodeUseCase by inject()
	private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()

	fun smsCodeEntered(
		phoneNumber: String,
		smsCode: String,
		referenceId: String,
	) {
		launchOperation {
			checkSmsCodeUseCase.execute(CheckSmsCodeUseCase.Params(
				phoneNumber = phoneNumber,
				verificationCode = smsCode,
				referenceId = referenceId
			)).map { CheckSmsCodeInfo.Check(it) }
		}
		//TODO: delete mockup
//		state.handle(CheckSmsCodeInfo.Check(smsCode == "0000"))
	}

	fun smsCodeReRequest(timeout : Int, phoneNumber: String) {
		startTimeout(timeout)
		launchOperation {
			checkPhoneNumberUseCase.execute(phoneNumber).map {
				CheckSmsCodeInfo.ReRequest(it.success)
			}
		}
	}

	fun startTimeout(timeout : Int) {
		var timer = timeout
		launchOperation {
			while (timer >= 0) {
				state.handle(CheckSmsCodeInfo.Timeout(timer))
				delay(1000)
				timer--
			}
			Either.Right(CheckSmsCodeInfo.EnableReRequest)
		}
	}

}