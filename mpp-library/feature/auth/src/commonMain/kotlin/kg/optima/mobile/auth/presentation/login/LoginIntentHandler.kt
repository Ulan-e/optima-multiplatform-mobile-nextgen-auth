package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.auth.presentation.login.utils.toUseCaseModel
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class LoginIntentHandler(
	override val stateMachine: LoginStateMachine,
) : IntentHandler<LoginIntentHandler.LoginIntent, LoginModel>() {

	private val loginUseCase: LoginUseCase by inject()
	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun dispatch(intent: LoginIntent) {
		val operation: suspend () -> Either<Failure, LoginModel> = {
			when (intent) {
				is LoginIntent.SignIn -> signIn(intent)
				LoginIntent.GetClientId -> getClientId()
				LoginIntent.ShowBiometry -> showBiometry()
			}
		}

		launchOperation(operation = operation)
	}

	private suspend fun signIn(intent: LoginIntent.SignIn) =
		loginUseCase.execute(intent.toUseCaseModel())

	private suspend fun getClientId() =
		clientInfoUseCase.execute(ClientInfoUseCase.Params).map { LoginModel.ClientId(it.clientId) }

	private suspend fun showBiometry() = clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
		LoginModel.Biometry(enabled = it.grantTypes.contains(GrantType.Biometry))
	}

	sealed interface LoginIntent : Intent {
		sealed interface SignIn : LoginIntent {
			class Password(
				val clientId: String,
				val password: String,
			) : SignIn

			class Pin(
				val pin: String,
			) : SignIn

			object Biometry : SignIn
		}

		object ShowBiometry : LoginIntent

		object GetClientId : LoginIntent
	}
}
