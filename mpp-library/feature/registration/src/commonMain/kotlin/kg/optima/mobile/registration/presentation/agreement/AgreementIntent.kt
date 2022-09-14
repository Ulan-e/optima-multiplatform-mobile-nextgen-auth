package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.base.presentation.State

class AgreementIntent(
	override val state: State<AgreementInfo>
) : Intent<AgreementInfo>() {

	fun confirm() {
		state.handle(AgreementInfo(true))
	}
}