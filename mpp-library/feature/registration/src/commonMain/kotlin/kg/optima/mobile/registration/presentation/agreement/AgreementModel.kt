package kg.optima.mobile.registration.presentation.agreement

import kg.optima.mobile.base.presentation.BaseEntity

sealed interface AgreementModel : BaseEntity {
	class AgreementInfo(
		val confirmed: Boolean,
	) : AgreementModel

	class Offerta(
		val url: String
	) : AgreementModel
}