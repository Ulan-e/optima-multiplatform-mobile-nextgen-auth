package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.common.domain.LaunchUseCase
import org.koin.core.component.inject

class LaunchIntent(
	override val state: LaunchState,
) : Intent<LaunchEntity>() {

	private val clientInfoUseCase: LaunchUseCase by inject()

	fun checkIsAuthorized() {
		launchOperation {
			clientInfoUseCase.execute(LaunchUseCase.Params).map {
				LaunchEntity.ClientInfo(
					isAuthorized = it.isAuthorized,
					clientId = it.clientId,
					grantTypes = it.grantTypes
				)
			}
		}
	}
}
