package kg.optima.mobile.auth.presentation.launch

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

class LaunchStateMachine : StateMachine<ClientInfo>() {

	override fun handle(entity: ClientInfo) {
		val screenModels = mutableListOf<ScreenModel>(WelcomeScreenModel.Welcome)
		if (entity.isAuthorized) {
			screenModels.add(AuthScreenModel.Login)
			screenModels.addAll(authScreenModels(entity.grantTypes))
		}

		setState(State.Navigate(screenModels))
	}

	private fun authScreenModels(grantTypes: List<GrantType>): List<ScreenModel> {
		val screenModels = mutableListOf<ScreenModel>()
		if (grantTypes.contains(GrantType.Pin)) {
			screenModels += AuthScreenModel.PinEnter(
				showBiometry = grantTypes.contains(GrantType.Biometry)
			)
		}

		return screenModels
	}
}