package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.presentation.BaseEntity
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

sealed interface CreatePasswordModel : BaseEntity {
    class Validate(
        val list: List<PasswordValidityModel>
    ) : CreatePasswordModel

    class Comparison(
        val matches: Boolean
    ) : CreatePasswordModel

    class RegisterSuccess(
        val message: String,
        val clientId: String?
    ) : CreatePasswordModel

    class RegisterFailed(
        val message: String
    ) : CreatePasswordModel

    object ReturnToMain : CreatePasswordModel

    object RegistrationDone : CreatePasswordModel
}