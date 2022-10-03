package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class AgreementState : BaseMppState<AgreementModel>() {

	override fun handle(entity: AgreementModel) {
		val stateModel = when (entity) {
			is AgreementModel.AgreementInfo -> {
				if (entity.confirmed) {
					StateModel.Navigate(RegistrationScreenModel.EnterPhone)
				} else {
					StateModel.Pop
				}
			}
			is AgreementModel.Offerta -> StateModel.Navigate(
				screenModel = RegistrationScreenModel.Offerta(entity.url)
			)
		}

		setStateModel(stateModel)
	}
}