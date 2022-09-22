package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.feature.welcome.WelcomeScreenModel
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

class CreatePasswordState : State<CreatePasswordModel>() {

    override fun handle(entity: CreatePasswordModel) {
        val stateModel: StateModel = when (entity) {
            is CreatePasswordModel.Validate -> CreatePasswordStateModel.ValidationResult(entity.list)
            is CreatePasswordModel.Comparison -> CreatePasswordStateModel.ComparisonResult(entity.matches)
            is CreatePasswordModel.RegisterSuccess -> CreatePasswordStateModel.RegisterSuccessResult(entity.clientId, entity.message)
            is CreatePasswordModel.RegisterFailed -> CreatePasswordStateModel.RegisterFailedResult(entity.message)
            CreatePasswordModel.RegistrationDone -> StateModel.Navigate(WelcomeScreenModel.Welcome)
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