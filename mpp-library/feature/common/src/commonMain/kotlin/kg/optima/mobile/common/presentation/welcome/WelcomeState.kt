package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.main.BottomNavScreenModel

class WelcomeState : BaseMppState<WelcomeEntity>() {

	override fun handle(entity: WelcomeEntity) {
		val stateModel = when (entity) {
			WelcomeEntity.Login -> Model.NavigateTo.Login(BottomNavScreenModel)
			WelcomeEntity.Register -> Model.NavigateTo.RegisterAgreement
		}

		setStateModel(stateModel)
	}

	sealed interface Model : StateModel {
		sealed interface NavigateTo : Model {
			class Login(val nextScreenModel: ScreenModel) : NavigateTo
			object RegisterAgreement : NavigateTo
		}
	}
}