package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.UiIntent

class AgreementIntent(
	override val uiState: AgreementState
) : UiIntent<AgreementModel>() {

	fun confirm() {
		uiState.handle(AgreementModel.AgreementInfo(true))
	}

	fun openOfferta() {
		uiState.handle(AgreementModel.Offerta(
			url = "https://forms.optimabank.kg/offers/o24-20220101-en.html"
		))
	}
}