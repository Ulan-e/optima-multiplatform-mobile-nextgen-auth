package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.base.presentation.State

class AgreementIntent(
	override val state: State<AgreementModel>
) : Intent<AgreementModel>() {

	fun confirm() {
		state.handle(AgreementModel.AgreementInfo(true))
	}

	fun openOfferta() {
		state.handle(AgreementModel.Offerta(
			url = "https://forms.optimabank.kg/offers/o24-20220101-en.html"
		))
	}
}