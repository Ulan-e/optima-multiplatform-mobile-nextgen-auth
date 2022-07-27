package kg.optima.mobile.auth.domain.usecase

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.data.repository.AuthRepository
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
    private val component: FeatureAuthComponent,
) : BaseUseCase<LoginUseCase.Params, LoginUseCase.Token>() {

    class Token(
        val jwt: String,
        val refreshToken: String = "",
        val refreshTokenExp: String = "",
    )

    override suspend fun execute(
        model: Params,
    ): Either<Failure, Token> {
        val request = UserAuthenticationRequest(
            deviceId = component.deviceId,
            mobile = model.mobile,
            password = model.password
        )
        return authRepository.login(request)
            .onSuccess {
                component.saveToken(it?.jwt.orEmpty())
                component.refreshToken = it?.refreshToken
                component.isAuthorized = true
            }
            .map {
                Token(
                    jwt = it?.jwt.orEmpty(),
                    refreshToken = it?.refreshToken.orEmpty(),
                    refreshTokenExp = it?.refreshTokenExp.orEmpty()
                )
            }
    }

    class Params(
        val mobile: String,
        val password: String,
    )
}