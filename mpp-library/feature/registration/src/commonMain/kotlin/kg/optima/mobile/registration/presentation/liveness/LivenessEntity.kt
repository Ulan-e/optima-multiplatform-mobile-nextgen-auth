package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.BaseEntity

sealed interface LivenessEntity : BaseEntity {
	class Passed(
		val passed: Boolean,
		val message: String?
	) : LivenessEntity

	sealed interface NavigateTo : LivenessEntity {
		object Contacts : LivenessEntity
		object Welcome : LivenessEntity
		object ControlQuestion : LivenessEntity
		object SelfConfirm : LivenessEntity
	}
}