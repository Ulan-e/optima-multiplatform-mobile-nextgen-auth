package kg.optima.mobile.auth.presentation.setup_auth

import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel

class SetupAuthState(
	private val nextScreenModel: ScreenModel,
) : BaseMppState<SetupAuthResult>() {

	override fun handle(entity: SetupAuthResult) {
		val state = when (entity) {
			SetupAuthResult.Save -> SetupAuthStateModel.SavePin
			is SetupAuthResult.Compare -> SetupAuthStateModel.ComparePin(entity.isMatch)
			SetupAuthResult.Done -> StateModel.Navigate(nextScreenModel)
		}

		setStateModel(state)
	}

	sealed interface SetupAuthStateModel : StateModel {
		object SavePin : SetupAuthStateModel
		class ComparePin(val isMatches: Boolean) : SetupAuthStateModel
	}
}