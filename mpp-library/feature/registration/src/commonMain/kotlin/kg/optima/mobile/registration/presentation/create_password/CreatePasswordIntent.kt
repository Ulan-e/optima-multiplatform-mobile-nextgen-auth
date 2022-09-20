package kg.optima.mobile.registration.presentation.create_password

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.model.RegisterClientEntity
import kg.optima.mobile.registration.domain.usecase.RegistrationUseCase
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidator
import org.koin.core.component.inject

class CreatePasswordIntent(
	override val state: CreatePasswordState
) : Intent<CreatePasswordModel>() {

	private val registrationUseCase: RegistrationUseCase by inject()

	fun validate(password: String) {
		val model = CreatePasswordModel.Validate(PasswordValidator.validate(password))
		state.handle(model)
	}

	fun compare(password: String, repassword: String) {
		val model = CreatePasswordModel.Comparison(PasswordValidator.compare(password, repassword))
		state.handle(model)
	}

	fun submit(
		hash: String,
		password: String,
		questionId: String,
		answer: String,
	) {
		launchOperation {
//			registrationUseCase.execute(
//				RegistrationUseCase.Params(
//					hash = hash,
//					hashPassword = CryptographyUtils.getHash(password),
//					questionId = questionId,
//					answer = answer
//				)
//			)
			Either.Right(RegisterClientEntity(
				success = true,
				message = "Поздравляем! \u2028Вы зарегистрированы в Optima24",
				clientId = "123456"
			)).map {
				CreatePasswordModel.Register(
					message = it.message,
					clientId = it.clientId
				)
			}
		}
	}

	fun onRegistrationDone() {
		state.handle(CreatePasswordModel.RegistrationDone)
	}
}