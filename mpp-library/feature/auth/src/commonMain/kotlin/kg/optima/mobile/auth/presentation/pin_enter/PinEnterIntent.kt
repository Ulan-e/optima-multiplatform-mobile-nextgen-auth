package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.domain.usecase.logout.LogoutUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.base.data.model.map
import org.koin.core.component.inject

open class PinEnterIntent(
	override val uiState: PinEnterState,
) : LoginIntent<PinEnterEntity>(uiState) {

	private val logoutUseCase: LogoutUseCase by inject()

	fun logout() {
		launchOperation {
			logoutUseCase.execute(LogoutUseCase.Params()).map { PinEnterEntity.Logout }
		}
	}
}
