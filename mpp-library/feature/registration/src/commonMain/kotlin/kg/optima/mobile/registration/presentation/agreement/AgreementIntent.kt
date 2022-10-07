package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.BaseMppIntent

class AgreementIntent(
	override val mppState: AgreementState
) : BaseMppIntent<AgreementModel>() {

	fun confirm() {
		mppState.handle(AgreementModel.AgreementInfo(true))
	}

	fun openOfferta() {
		mppState.handle(AgreementModel.Offerta(
			url = "https://forms.optimabank.kg/offers/o24-20220101-en.html"
		))
	}
}