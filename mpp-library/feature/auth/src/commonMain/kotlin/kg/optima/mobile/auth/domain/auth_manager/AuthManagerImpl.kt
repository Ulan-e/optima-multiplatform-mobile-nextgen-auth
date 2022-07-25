package kg.optima.mobile.auth.domain.auth_manager

import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.presentation.state.AuthStateMachine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthManagerImpl : AuthManager, KoinComponent {

    private val component: FeatureAuthComponent by inject()

    override fun isAuthorized(): Boolean {
        return component.isAuthorized
    }

    val m = AuthStateMachine()

    override fun isAuthorized(authorized: () -> Unit, notAuthorized: () -> Unit) {
        if (component.isAuthorized) {
            authorized()
        } else {
            notAuthorized()
        }
    }


}
