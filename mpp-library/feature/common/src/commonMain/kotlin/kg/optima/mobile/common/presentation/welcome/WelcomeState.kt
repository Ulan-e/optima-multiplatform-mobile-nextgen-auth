package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.auth.model.GrantType
import kg.optima.mobile.feature.main.BottomNavScreenModel
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class WelcomeState : BaseMppState<WelcomeEntity>() {

	override fun handle(entity: WelcomeEntity) {
		val stateModel = when (entity) {
			is WelcomeEntity.ClientInfo -> clientInfo(entity)
			WelcomeEntity.Register -> StateModel.Navigate(RegistrationScreenModel.Agreement)
		}

		setStateModel(stateModel)
	}

	private fun clientInfo(entity: WelcomeEntity.ClientInfo): StateModel.Navigate {
		val screenModels = mutableListOf<ScreenModel>()
		val nextScreenModel = BottomNavScreenModel
		if (entity.isAuthorized) {
			screenModels.add(AuthScreenModel.Login(nextScreenModel))
			screenModels.addAll(authScreenModels(entity.grantTypes, nextScreenModel))
		} else {
			screenModels.add(
				AuthScreenModel.Login(
					nextScreenModel = AuthScreenModel.PinSet(
						nextScreenModel = nextScreenModel,
					)
				)
			)
		}

		return StateModel.Navigate(screenModels)
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