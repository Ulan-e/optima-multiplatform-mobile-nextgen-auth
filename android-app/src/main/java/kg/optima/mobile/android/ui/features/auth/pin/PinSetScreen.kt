package kg.optima.mobile.android.ui.features.auth.pin

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthIntentHandler
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthStateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.setPinScreenHeader

class PinSetScreen(
	private val nextScreenModel: ScreenModel,
) : Screen {
	@Composable
	override fun Content() {
		val model = remember {
			AuthFeatureFactory.create<SetupAuthIntentHandler, SetupAuthStateMachine>(nextScreenModel)
		}
		val stateMachine = model.stateMachine
		val intentHandler = model.intentHandler

		val state by stateMachine.state.collectAsState(initial = null)

		val headerState = remember { mutableStateOf("Установить новый PIN-код") }
		val subheaderState = remember { mutableStateOf("для быстрого входа в приложение") }
		val codeState = remember { mutableStateOf(emptyString) }

		when (val pinSetState = state) {
			is SetupAuthStateMachine.SetupAuthState -> {
				when (pinSetState) {
					SetupAuthStateMachine.SetupAuthState.SavePin -> {
						headerState.value = "Повторить PIN-код"
					}
					is SetupAuthStateMachine.SetupAuthState.ComparePin -> {
						if (pinSetState.isMatches) {
							// TODO showSetBiometry
							intentHandler.dispatch(
								SetupAuthIntentHandler.SetupAuthIntent.Biometry(true)
							)
						} else {
							// TODO pin not matches
						}
					}
				}
			}
		}

		MainContainer(mainState = state) {
			PinScreen(
				header = setPinScreenHeader(headerState.value, subheaderState.value),
				codeState = codeState,
				onInputCompleted = {
					when (state) {
						null -> {
							intentHandler.dispatch(
								SetupAuthIntentHandler.SetupAuthIntent.SavePin(codeState.value)
							)
							codeState.value = emptyString
						}
						SetupAuthStateMachine.SetupAuthState.SavePin ->
							intentHandler.dispatch(
								SetupAuthIntentHandler.SetupAuthIntent.ComparePin(codeState.value)
							)
					}
				},
				actionCell = ActionCell.Close {

				}
			)
		}
	}
}