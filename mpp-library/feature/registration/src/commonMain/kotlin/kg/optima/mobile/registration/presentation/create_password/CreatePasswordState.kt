package kg.optima.mobile.registration.presentation.create_password

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

class CreatePasswordState : UiState<CreatePasswordModel>() {

	override fun handle(entity: CreatePasswordModel) {
		val stateModel: UiState.Model = when (entity) {
			is CreatePasswordModel.Validate -> Model.ValidationResult(entity.list)
			is CreatePasswordModel.Comparison -> Model.ComparisonResult(entity.matches)
			is CreatePasswordModel.RegisterSuccess -> Model.RegisterSuccessResult(
				clientId = entity.clientId,
				message = entity.message
			)
			is CreatePasswordModel.RegisterFailed -> Model.RegisterFailedResult(
				entity.message
			)
			CreatePasswordModel.RegistrationDone ->
				Model.NavigateTo.RegistrationInterview(
					url = "https://docs.google.com/forms/d/10xKZzz7I2N0kJQjsseCa2-5QNAwL9O-DNnnX4RRwP9U"
				)
			CreatePasswordModel.ReturnToMain -> Model.NavigateTo.Welcome
		}
		setStateModel(stateModel)
	}

	sealed interface Model : UiState.Model {
		class ValidationResult(
			val validityModels: List<PasswordValidityModel>
		) : Model

		class ComparisonResult(
			val matches: Boolean
		) : Model

		class RegisterSuccessResult(
			val clientId: String?,
			val message: String,
		) : Model

		class RegisterFailedResult(
			val message: String,
		) : Model

		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			class RegistrationInterview(
				val url: String
			) : NavigateTo
		}
	}
}