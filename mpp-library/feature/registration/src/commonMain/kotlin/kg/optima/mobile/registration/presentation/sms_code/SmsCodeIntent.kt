package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.usecase.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.domain.usecase.CheckSmsCodeUseCase
import kotlinx.coroutines.delay
import org.koin.core.component.inject

class SmsCodeIntent(
	override val state: SmsCodeState,
) : Intent<CheckSmsCodeInfo>() {

	private val checkSmsCodeUseCase: CheckSmsCodeUseCase by inject()
	private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()

	private var timeLeft = 0L
	private var finishTime = 0L
	private var timer = 0
	private var timerPaused = false

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
			).map { CheckSmsCodeInfo.OtpCheck(it.success) }
		}
	}

	fun smsCodeRequest(phoneNumber: String, currentTime: Long) {
		launchOperation {
			checkPhoneNumberUseCase.execute(phoneNumber).map {
				timeLeft = it.timeLeft
				startTimer(timeLeft, currentTime)
				CheckSmsCodeInfo.Check(it.success)
			}
		}
	}

	fun pauseTimer() {
		timer = 0
		timerPaused = true
	}

	fun startTimer(timeLeft: Long, currentTime: Long) {
		if (timerPaused) {
			timerPaused = false
			val timeLeftAfterPause = if ((finishTime - currentTime) > 0L) { finishTime - currentTime } else { 0L }
			timer = (timeLeftAfterPause / 1000).toInt()
		} else {
			finishTime = currentTime + timeLeft
			timer = (timeLeft / 1000).toInt()
		}
		launchOperation {
			while (timer > 0) {
				state.handle(CheckSmsCodeInfo.TimeLeft(timer))
				delay(1000)
				timer--
			}
			Either.Right(CheckSmsCodeInfo.TimeLeft(timer))
		}
	}

//	fun startTimer(timeLeft: Long) {
//		var timer = (timeLeft / 1000).toInt()
//		launchOperation {
//			while (timer > 0) {
//				state.handle(CheckSmsCodeInfo.TimeLeft(timer))
//				delay(1000)
//				timer--
//			}
//			Either.Right(CheckSmsCodeInfo.TimeLeft(timer))
//		}
//	}
}