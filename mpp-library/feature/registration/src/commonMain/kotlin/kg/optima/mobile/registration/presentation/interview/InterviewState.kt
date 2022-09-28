package kg.optima.mobile.registration.presentation.interview

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

class InterviewState : State<InterviewInfo>() {

	override fun handle(entity: InterviewInfo) {
		val stateModel = when (entity) {
			is InterviewInfo.toWelcomeScreen -> StateModel.Navigate(WelcomeScreenModel.Welcome)
		}
		setStateModel(stateModel)
	}
}