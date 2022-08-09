package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.domain.CryptographyUtils
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.auth.presentation.login.model.toPlainModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class LoginIntentHandler(
	override val stateMachine: LoginStateMachine,
) : IntentHandler<LoginIntentHandler.LoginIntent, LoginModel>() {

	private val loginUseCase: LoginUseCase by inject()

	override fun dispatch(intent: LoginIntent) {
		val operation: suspend () -> Either<Failure, LoginModel> = {
			when (intent) {
				is LoginIntent.SignIn -> signIn(intent)
			}
		}

		launchOperation(operation = operation)
	}

	private suspend fun signIn(intent: LoginIntent.SignIn): Either<Failure, LoginModel.LoginResponse> {
		val model = LoginUseCase.Params(
			clientId = intent.clientId,
			password = CryptographyUtils.getHash(intent.password),
			grantType = intent.grantType,
		)

		return loginUseCase.execute(model).map(LoginResponse::toPlainModel)
	}


	sealed interface LoginIntent : Intent {
		class SignIn(
			val clientId: String,
			val password: String,
			val grantType: GrantType,
		) : LoginIntent
	}
}
