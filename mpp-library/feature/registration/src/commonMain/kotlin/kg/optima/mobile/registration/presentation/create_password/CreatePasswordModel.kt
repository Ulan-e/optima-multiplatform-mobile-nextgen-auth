package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

sealed interface CreatePasswordModel {
    class Validate(
        val list: List<PasswordValidityModel>
    ) : CreatePasswordModel

    class Comparison(
        val matches: Boolean
    ) : CreatePasswordModel

    class Register(
        val message: String,
        val clientId: String?
    ) : CreatePasswordModel
}