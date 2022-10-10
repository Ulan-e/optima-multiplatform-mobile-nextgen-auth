package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.domain.usecase.logout.LogoutUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.feature.auth.component.AuthPreferences
import org.koin.core.component.inject

class PinEnterIntent(
    override val uiState: PinEnterState,
) : LoginIntent(uiState) {

    private val logoutUseCase: LogoutUseCase by inject()
    private val authPreferences: AuthPreferences by inject()

    fun logout() {
        launchOperation {
            logoutUseCase.execute(LogoutUseCase.Params()).map { PinEnterEntity.Logout }
        }
    }

    fun username() = authPreferences.userInfo?.firstName ?: ""

    fun attempts() = authPreferences.pinAttempts

    fun decreaseAttempts() {
        authPreferences.pinAttempts = attempts().dec()
    }
}