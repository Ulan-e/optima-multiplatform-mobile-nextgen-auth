package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.auth.data.component.AuthPreferences
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.domain.BaseUseCase
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
			is Params.Password -> signIn(model.grantType, model.clientId, model.password)
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
		grantType: GrantType = GrantType.Password,
		clientId: String = authPreferences.clientId.orEmpty(),
		password: String = authPreferences.password,
	): Either<Failure, LoginModel.Success> {
		val response: Either<Failure, LoginResponse> = if (clientId == "371564" && password == "killme123") {
			Either.Right(LoginResponse(
				accessToken = "",
				expiresIn = 0,
				refreshToken = "",
				refreshTokenExpiresIn = 0,
				sessionState = "",
			))
		} else {
			Either.Left(Failure.Message("Неверный пароль"))
		}

//		val request = UserAuthenticationRequest(
//			grantType = grantType,
//			clientId = clientId,
//			password = password,
//		)
//		return authRepository.login(request.map)
		val firstAuth = !authPreferences.isAuthorized
		return response
			.onSuccess {
				authPreferences.clientId = clientId
				authPreferences.password = password
				authPreferences.saveToken(it.accessToken)
				authPreferences.refreshToken = it.refreshToken
				authPreferences.isAuthorized = true
			}
			.map { LoginModel.Success(firstAuth) }
	}

	sealed interface Params {
		class Password(
			val clientId: String,
			val password: String,
		) : Params {
			val grantType = GrantType.Password
		}

		class Pin(
			val pin: String
		) : Params

		object Biometry : Params
	}
}