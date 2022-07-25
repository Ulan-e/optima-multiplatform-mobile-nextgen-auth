package kg.optima.mobile.auth.presentation.intent

import kg.optima.mobile.auth.domain.AuthModel
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.state.AuthState
import kg.optima.mobile.auth.presentation.state.AuthStateMachine
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.inject

class AuthIntentHandler : IntentHandler<AuthIntent, AuthModel, AuthState>() {

    private val loginUseCase: LoginUseCase by inject()

    override val stateMachine: AuthStateMachine by inject()

    override fun dispatch(intent: AuthIntent) {
        launchOperation<AuthModel> {
            when (intent) {
                is AuthIntent.Login -> login(intent)
            }
        }
    }

    private suspend fun CoroutineScope.login(intent: AuthIntent.Login): Either<Failure, AuthModel.Token> {
        val model = LoginUseCase.Params(
            mobile = intent.mobile,
            password = intent.password,
        )
        return Either.Right(AuthModel.Token(""))

//        return loginUseCase.execute(model, this)
    }

}
