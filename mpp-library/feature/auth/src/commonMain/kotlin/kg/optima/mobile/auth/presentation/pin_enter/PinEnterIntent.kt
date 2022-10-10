package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.domain.usecase.logout.LogoutUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.core.common.Constants
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

    fun pinAttempts() = authPreferences.pinAttempts

    fun decreasePinAttempts() {
        authPreferences.pinAttempts = pinAttempts() - 1
    }

    fun touchAttempts() = authPreferences.touchAttempts

    fun decreaseTouchAttempts() {
        authPreferences.touchAttempts = touchAttempts() - 1
    }

    fun navigateToLogin(){
        authPreferences.pinAttempts = Constants.MAX_ATTEMPTS
        uiState.handle(PinEnterEntity.NavigateTo.Login)
    }
}