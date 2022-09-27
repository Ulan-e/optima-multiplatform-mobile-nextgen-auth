package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.common.domain.LaunchUseCase
import org.koin.core.component.inject

class WelcomeIntent(
	override val state: WelcomeState,
) : Intent<WelcomeEntity>() {

	private val clientInfoUseCase: LaunchUseCase by inject()

	fun checkIsAuthorized() {
		launchOperation {
			clientInfoUseCase.execute(LaunchUseCase.Params).map {
				WelcomeEntity.ClientInfo(
					isAuthorized = it.isAuthorized,
					clientId = it.clientId,
					grantTypes = it.grantTypes
				)
			}
		}
	}

	fun register() {
		state.handle(WelcomeEntity.Register)
	}
}
