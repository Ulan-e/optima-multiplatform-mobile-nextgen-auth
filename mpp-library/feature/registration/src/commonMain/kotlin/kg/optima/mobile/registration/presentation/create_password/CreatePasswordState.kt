package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.common.CommonScreenModel
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

class CreatePasswordState : BaseMppState<CreatePasswordModel>() {

	override fun handle(entity: CreatePasswordModel) {
		val stateModel: StateModel = when (entity) {
			is CreatePasswordModel.Validate -> CreatePasswordStateModel.ValidationResult(entity.list)
			is CreatePasswordModel.Comparison -> CreatePasswordStateModel.ComparisonResult(entity.matches)
			is CreatePasswordModel.RegisterSuccess -> CreatePasswordStateModel.RegisterSuccessResult(
				entity.clientId,
				entity.message
			)
			is CreatePasswordModel.RegisterFailed -> CreatePasswordStateModel.RegisterFailedResult(
				entity.message
			)
			CreatePasswordModel.RegistrationDone ->
				StateModel.Navigate(
					CommonScreenModel.Interview(
						url = "https://docs.google.com/forms/d/10xKZzz7I2N0kJQjsseCa2-5QNAwL9O-DNnnX4RRwP9U"
					)
				)
		}
		setStateModel(stateModel)
	}

	sealed interface CreatePasswordStateModel : StateModel {
		class ValidationResult(
			val validityModels: List<PasswordValidityModel>
		) : CreatePasswordStateModel

		class ComparisonResult(
			val matches: Boolean
		) : CreatePasswordStateModel

		class RegisterSuccessResult(
			val clientId: String?,
			val message: String,
		) : CreatePasswordStateModel

		class RegisterFailedResult(
			val message: String,
		) : CreatePasswordStateModel
	}
}