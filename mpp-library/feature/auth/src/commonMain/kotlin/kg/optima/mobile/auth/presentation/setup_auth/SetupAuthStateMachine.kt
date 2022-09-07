package kg.optima.mobile.auth.presentation.setup_auth

import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.main.MainScreenModel

class SetupAuthStateMachine(
	private val nextScreenModel: ScreenModel,
) : StateMachine<SetupAuthResult>() {

	sealed interface SetupAuthState : State {
		object SavePin : SetupAuthState
		class ComparePin(val isMatches: Boolean) : SetupAuthState
	}

	override fun handle(entity: SetupAuthResult) {
		val state = when (entity) {
			SetupAuthResult.Save -> SetupAuthState.SavePin
			is SetupAuthResult.Compare -> SetupAuthState.ComparePin(entity.isMatch)
			SetupAuthResult.Done -> State.Navigate(nextScreenModel)
		}

		setState(state)
	}
}