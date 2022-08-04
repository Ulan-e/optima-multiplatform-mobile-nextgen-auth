package kg.optima.mobile.auth.presentation.pin_set

import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetResult
import kg.optima.mobile.base.presentation.StateMachine

class PinSetStateMachine : StateMachine<PinSetResult>() {

	sealed interface PinSetState : State {
		object Save : PinSetState
		class Compare(val isMatches: Boolean) : PinSetState
	}

	override fun handle(entity: PinSetResult) {
		val state = when (entity) {
			PinSetResult.Save -> PinSetState.Save
			is PinSetResult.Compare -> PinSetState.Compare(entity.result)
		}

		setState(state)
	}
}