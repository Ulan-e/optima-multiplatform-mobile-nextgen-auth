package kg.optima.mobile.registration.presentation.interview

import kg.optima.mobile.base.presentation.Intent

class InterviewIntent(
	override val state: InterviewState
) : Intent<InterviewInfo>() {

	fun navigate() {
		state.handle(InterviewInfo.toWelcomeScreen)
	}

}