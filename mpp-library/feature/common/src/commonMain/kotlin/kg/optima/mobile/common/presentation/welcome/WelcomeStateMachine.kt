package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.main.MainScreenModel

class WelcomeStateMachine : StateMachine<ClientInfo>() {

	override fun handle(entity: ClientInfo) {
		val screenModels = mutableListOf<ScreenModel>()
		if (entity.isAuthorized) {
			val nextScreenModel = MainScreenModel.Main
			screenModels.add(AuthScreenModel.Login(nextScreenModel))
			screenModels.addAll(authScreenModels(entity.grantTypes, nextScreenModel))
		} else {
			screenModels.add(AuthScreenModel.Login(nextScreenModel = AuthScreenModel.PinSet))
		}

		setState(State.Navigate(screenModels))
	}

	private fun authScreenModels(
		grantTypes: List<GrantType>,
		nextScreenModel: ScreenModel,
	): List<ScreenModel> {
		val screenModels = mutableListOf<ScreenModel>()
		if (grantTypes.contains(GrantType.Pin)) {
			screenModels += AuthScreenModel.PinEnter(
				showBiometry = grantTypes.contains(GrantType.Biometry),
				nextScreenModel = nextScreenModel,
			)
		}

		return screenModels
	}
}