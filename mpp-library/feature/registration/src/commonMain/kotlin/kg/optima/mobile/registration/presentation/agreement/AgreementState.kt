package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class AgreementState : State<AgreementInfo>() {

	override fun handle(entity: AgreementInfo) {
		if (entity.confirmed) {
			setStateModel(StateModel.Navigate(RegistrationScreenModel.EnterPhone))
		} else {
			setStateModel(StateModel.Pop)
		}
	}
}