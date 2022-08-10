package kg.optima.mobile.android.ui.pin

import android.util.Log
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.main.MainScreen
import kg.optima.mobile.auth.presentation.pin_set.PinSetFactory
import kg.optima.mobile.auth.presentation.pin_set.PinSetIntentHandler
import kg.optima.mobile.auth.presentation.pin_set.PinSetStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.setPinScreenHeader

object PinSetScreen : Screen {
	@Composable
	override fun Content() {
		val stateMachine: PinSetStateMachine = PinSetFactory.stateMachine
		val intentHandler: PinSetIntentHandler = PinSetFactory.intentHandler

		val navigator = LocalNavigator.currentOrThrow

		val state by stateMachine.state.collectAsState(initial = null)

		val headerState = remember { mutableStateOf("Установить новый PIN-код") }
		val subheaderState = remember { mutableStateOf("для быстрого входа в приложение") }
		val codeState = remember { mutableStateOf(emptyString) }

		when (val pinSetState = state) {
			is PinSetStateMachine.PinSetState -> {
				when (pinSetState) {
					PinSetStateMachine.PinSetState.Save -> {
						headerState.value = "Повторить PIN-код"
					}
					is PinSetStateMachine.PinSetState.Compare -> {
						if (pinSetState.isMatches) {
							navigator.replaceAll(MainScreen)
						}
					}
				}
			}
			is StateMachine.State.Loading ->
				Log.d("MainScreen", "Loading State")
			is StateMachine.State.Error ->
				Log.d("MainScreen", "Error State")
		}
		PinScreen(
			header = setPinScreenHeader(headerState.value, subheaderState.value),
			codeState = codeState,
			onInputCompleted = {
				when (state) {
					null -> {
						intentHandler.dispatch(PinSetIntentHandler.PinSetIntent.Save(codeState.value))
						codeState.value = emptyString
					}
					PinSetStateMachine.PinSetState.Save ->
						intentHandler.dispatch(PinSetIntentHandler.PinSetIntent.Compare(codeState.value))
				}
			},
		)
	}
}