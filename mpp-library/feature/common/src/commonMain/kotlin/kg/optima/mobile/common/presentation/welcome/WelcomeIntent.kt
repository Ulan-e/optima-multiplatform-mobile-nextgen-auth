package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.BaseMppIntent
import org.koin.core.component.inject

class WelcomeIntent(
	override val mppState: WelcomeState,
) : BaseMppIntent<WelcomeEntity>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	fun checkIsAuthorized() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
				WelcomeEntity.ClientInfo(
					isAuthorized = it.isAuthorized,
					clientId = it.clientId,
					grantTypes = it.grantTypes
				)
			}
		}
	}

	fun register() {
		mppState.handle(WelcomeEntity.Register)
	}
}
