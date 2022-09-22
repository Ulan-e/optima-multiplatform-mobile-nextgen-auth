package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.auth.data.component.AuthPreferences
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.common.CryptographyUtils
import kg.optima.mobile.core.error.Failure

/* TODO
   1. Refactor saving params with throwing errors and handling them.
   2. Is saving token model to storage in UseCase correct?
*/
class LoginUseCase(
	private val authRepository: AuthRepository,
	private val authPreferences: AuthPreferences,
) : BaseUseCase<LoginUseCase.Params, LoginModel.Success>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, LoginModel.Success> {
		return when (model) {
			is Params.Biometry -> signIn()
			is Params.Password -> signIn(model.clientId, model.password, model.smsCode)
			is Params.Pin -> {
				if (model.pin == authPreferences.pin) {
					signIn()
				} else {
					Either.Left(Failure.Message("Неверный pin"))
				}
			}
		}
	}

	private suspend fun signIn(
		clientId: String = authPreferences.clientId.orEmpty(),
		password: String = authPreferences.password,
		smsCode: String? = null,
	): Either<Failure, LoginModel.Success> {
		val request = UserAuthenticationRequest(
			clientId = clientId,
			password = CryptographyUtils.getHash(password),
			smsCode = smsCode,
		)
		val firstAuth = !authPreferences.isAuthorized

		return authRepository.login(request).map { LoginModel.Success(firstAuth) }
	}

	sealed interface Params {
		class Password(
			val clientId: String,
			val password: String,
			val smsCode: String?
		) : Params

		class Pin(
			val pin: String
		) : Params

		object Biometry : Params
	}
}