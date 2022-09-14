package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

class CreatePasswordState : State<CreatePasswordModel>() {
	override fun handle(entity: CreatePasswordModel) {
		when (entity) {
			is CreatePasswordModel.Validate -> CreatePasswordStateModel.ValidationResult(entity.list)
			is CreatePasswordModel.Comparison -> CreatePasswordStateModel.ComparisonResult(entity.matches)
		}
	}

	sealed interface CreatePasswordStateModel : StateModel {
		class ValidationResult(
			val validityModels: List<PasswordValidityModel>
		) : CreatePasswordStateModel

		class ComparisonResult(
			val matches: Boolean
		) : CreatePasswordStateModel
	}
}