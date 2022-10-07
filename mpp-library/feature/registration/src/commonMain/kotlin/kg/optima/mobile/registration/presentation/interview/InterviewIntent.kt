package kg.optima.mobile.registration.presentation.interview

import kg.optima.mobile.base.presentation.UiIntent

class InterviewIntent(
	override val uiState: InterviewState
) : UiIntent<InterviewEntity>() {

	fun close() {
		uiState.handle(InterviewEntity)
	}

}