package kg.optima.mobile.registration.presentation.interview

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

class InterviewState : BaseMppState<InterviewInfo>() {

	override fun handle(entity: InterviewInfo) {
		val stateModel = when (entity) {
			is InterviewInfo.toWelcomeScreen -> StateModel.Navigate(WelcomeScreenModel.Welcome)
		}
		setStateModel(stateModel)
	}
}