package kg.optima.mobile.registration.presentation.agreement

sealed interface AgreementModel {
	class AgreementInfo(
		val confirmed: Boolean,
	) : AgreementModel

	class Offerta(
		val url: String
	) : AgreementModel
}