package kg.optima.mobile.common.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.UiIntent
import kotlinx.coroutines.delay

open class SmsCodeIntent(
	override val uiState: SmsCodeState,
) : UiIntent<CheckSmsCodeInfo>() {
	private var finishTime = 0L
	private var timer = 0
	private var timerPaused = false

	fun startTimer(timeLeft: Long, currentTime: Long) {
		if (timerPaused) {
			timerPaused = false
			val timeLeftAfterPause =
				if ((finishTime - currentTime) > 0L) finishTime - currentTime else 0L

			timer = (timeLeftAfterPause / 1000).toInt()
		} else {
			finishTime = currentTime + timeLeft
			timer = (timeLeft / 1000).toInt()
		}
		launchOperation {
			while (timer > 0) {
				uiState.handle(CheckSmsCodeInfo.TimeLeft(timer))
				delay(1000)
				timer--
			}
			Either.Right(CheckSmsCodeInfo.TimeLeft(timer))
		}
	}

	fun pauseTimer() {
		timer = 0
		timerPaused = true
	}
}