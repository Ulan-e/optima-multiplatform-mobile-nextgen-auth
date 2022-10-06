package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.auth.model.GrantType
import kg.optima.mobile.feature.main.BottomNavScreenModel
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

class LaunchState : BaseMppState<LaunchEntity>() {

	override fun handle(entity: LaunchEntity) {
		val screenModels = mutableListOf<ScreenModel>()

		when (entity) {
			is LaunchEntity.ClientInfo -> {
				screenModels.add(WelcomeScreenModel.Welcome)
				if (entity.isAuthorized) {
					val nextScreenModel = BottomNavScreenModel
					screenModels.add(AuthScreenModel.Login(nextScreenModel))
					screenModels.addAll(authScreenModels(entity.grantTypes, nextScreenModel))
				}
			}
			is LaunchEntity.OpenNextScreen -> {
				screenModels.add(entity.screenModel)
			}
		}

		setStateModel(StateModel.Navigate(screenModels))
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