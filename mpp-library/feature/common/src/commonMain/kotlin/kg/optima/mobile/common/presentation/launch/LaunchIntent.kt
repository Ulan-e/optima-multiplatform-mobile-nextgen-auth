package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import org.koin.core.component.inject

class LaunchIntent(
	override val state: LaunchState,
) : Intent<LaunchEntity>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	fun checkIsAuthorized() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
				LaunchEntity.ClientInfo(
					isAuthorized = it.isAuthorized,
					clientId = it.clientId,
					grantTypes = it.grantTypes
				)
			}
		}
	}
}
