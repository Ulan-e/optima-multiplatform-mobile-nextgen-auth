package kg.optima.mobile.registration.presentation.create_password

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

class CreatePasswordState : UiState<CreatePasswordModel>() {

	override fun handle(entity: CreatePasswordModel) {
		val stateModel: Model = when (entity) {
			is CreatePasswordModel.Validate -> CreatePasswordStateModel.ValidationResult(entity.list)
			is CreatePasswordModel.Comparison -> CreatePasswordStateModel.ComparisonResult(entity.matches)
			is CreatePasswordModel.RegisterSuccess -> CreatePasswordStateModel.RegisterSuccessResult(
				clientId = entity.clientId,
				message = entity.message
			)
			is CreatePasswordModel.RegisterFailed -> CreatePasswordStateModel.RegisterFailedResult(
				entity.message
			)
			CreatePasswordModel.RegistrationDone ->
				CreatePasswordStateModel.NavigateTo.RegistrationInterview(
					url = "https://docs.google.com/forms/d/10xKZzz7I2N0kJQjsseCa2-5QNAwL9O-DNnnX4RRwP9U"
				)
			CreatePasswordModel.ReturnToMain -> CreatePasswordStateModel.NavigateTo.Welcome
		}
		setStateModel(stateModel)
	}

	sealed interface CreatePasswordStateModel : Model {
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

		sealed interface NavigateTo : CreatePasswordStateModel, Model.Navigate {
			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			class RegistrationInterview(
				val url: String
			) : NavigateTo
		}
	}
}