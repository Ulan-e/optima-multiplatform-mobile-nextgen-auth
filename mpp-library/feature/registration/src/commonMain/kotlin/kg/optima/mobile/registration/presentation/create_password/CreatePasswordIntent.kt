package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.BaseMppIntent
import kg.optima.mobile.core.common.CryptographyUtils
import kg.optima.mobile.registration.domain.usecase.RegistrationUseCase
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidator
import org.koin.core.component.inject

class CreatePasswordIntent(
    override val mppState: CreatePasswordState
) : BaseMppIntent<CreatePasswordModel>() {

    private val registrationUseCase: RegistrationUseCase by inject()

    fun validate(password: String, repassword: String) {
        val model = CreatePasswordModel.Validate(PasswordValidator.validate(password, repassword))
        mppState.handle(model)
    }

    fun compare(password: String, repassword: String) {
        val model = CreatePasswordModel.Comparison(PasswordValidator.compare(password, repassword))
        mppState.handle(model)
    }

    fun register(hash: String, password: String, questionId: String, answer: String) {
        launchOperation {
            registrationUseCase.execute(
                RegistrationUseCase.Params(
                    hash = hash,
                    hashPassword = CryptographyUtils.getHash(password),
                    questionId = questionId,
                    answer = answer
                )
            ).map {
                if (it.success) {
                    CreatePasswordModel.RegisterSuccess(
                        message = it.message,
                        clientId = it.clientId
                    )
                } else {
                    CreatePasswordModel.RegisterFailed(
                        message = it.message
                    )
                }
            }
        }
    }

    fun onRegistrationDone() {
        mppState.handle(CreatePasswordModel.RegistrationDone)
    }
}