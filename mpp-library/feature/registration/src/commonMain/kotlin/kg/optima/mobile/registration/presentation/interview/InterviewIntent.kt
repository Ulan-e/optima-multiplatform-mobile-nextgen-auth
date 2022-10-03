package kg.optima.mobile.registration.presentation.interview

import kg.optima.mobile.base.presentation.BaseMppIntent

class InterviewIntent(
	override val mppState: InterviewState
) : BaseMppIntent<InterviewInfo>() {

	fun navigate() {
		mppState.handle(InterviewInfo.toWelcomeScreen)
	}

}