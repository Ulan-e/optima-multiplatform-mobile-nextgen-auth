package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

/* TODO
   1. Refactor saving params with throwing errors and handling them.
   2. Is saving token model to storage in UseCase correct?
*/
class LoginUseCase(
	private val authRepository: AuthRepository,
	private val component: FeatureAuthComponent,
) : BaseUseCase<LoginUseCase.Params, LoginResponse>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, LoginResponse> {
		val request = UserAuthenticationRequest(
			grantType = model.grantType,
			clientId = model.clientId,
			password = model.password
		)
		return authRepository.login(request.map)
			.onSuccess {
				component.clientId = model.clientId
				component.saveToken(it.accessToken)
				component.refreshToken = it.refreshToken
				component.isAuthorized = true
			}
	}

	class Params(
		val clientId: String,
		val password: String,
		val grantType: GrantType,
	)
}