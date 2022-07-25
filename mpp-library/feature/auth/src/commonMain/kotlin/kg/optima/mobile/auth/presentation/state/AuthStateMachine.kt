package kg.optima.mobile.auth.presentation.state

import kg.optima.mobile.auth.domain.AuthModel
import kg.optima.mobile.base.presentation.StateMachine

class AuthStateMachine : StateMachine<AuthModel, AuthState>() {
    override fun handle(model: AuthModel) {
        when (model) {
            is AuthModel.Token -> setState(AuthState.Login)
        }
    }
}