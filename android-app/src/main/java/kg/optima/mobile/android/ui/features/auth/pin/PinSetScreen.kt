package kg.optima.mobile.android.ui.features.auth.pin

import androidx.compose.runtime.*
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthIntent
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.setPinScreenHeader

class PinSetScreen(
	private val nextScreenModel: ScreenModel,
) : BaseScreen {
	@Composable
	override fun Content() {
		val product = remember {
			AuthFeatureFactory.create<SetupAuthIntent, SetupAuthState>(nextScreenModel)
		}
		val state = product.state
		val intent = product.intent

		val model by state.stateFlow.collectAsState(initial = null)

		val headerState = remember { mutableStateOf("Установить новый PIN-код") }
		val subheaderState = remember { mutableStateOf("для быстрого входа в приложение") }
		val codeState = remember { mutableStateOf(emptyString) }

		when (val pinSetState = model) {
			is SetupAuthState.SetupAuthStateModel -> {
				when (pinSetState) {
					SetupAuthState.SetupAuthStateModel.SavePin -> {
						headerState.value = "Повторить PIN-код"
					}
					is SetupAuthState.SetupAuthStateModel.ComparePin -> {
						if (pinSetState.isMatches) {
							// TODO showSetBiometry
							intent.setBiometry(true)
						} else {
							// TODO pin not matches
						}
					}
				}
			}
		}

		MainContainer(mainState = model) {
			PinScreen(
				header = setPinScreenHeader(headerState.value, subheaderState.value),
				codeState = codeState,
				onInputCompleted = {
					when (model) {
						null -> {
							intent.savePin(codeState.value)
							codeState.value = emptyString
						}
						SetupAuthState.SetupAuthStateModel.SavePin -> {
							intent.comparePin(codeState.value)
						}
					}
				},
				actionCell = ActionCell.Close {

				}
			)
		}
	}
}