package kg.optima.mobile.android.ui.features.auth.pin

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.auth.presentation.login.LoginFactory
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager


class PinEnterScreen(
	private val showBiometry: Boolean = false,
) : Screen {

	@Composable
	override fun Content() {
		val stateMachine: LoginStateMachine = LoginFactory.stateMachine
		val intentHandler: LoginIntentHandler = LoginFactory.intentHandler

		val state by stateMachine.state.collectAsState(
			initial = if (showBiometry) LoginStateMachine.LoginState.ShowBiometry else null
		)

		val context = LocalContext.current

		val codeState = remember { mutableStateOf(emptyString) }

		when (state) {
			is LoginStateMachine.LoginState.ShowBiometry -> {
				BiometryManager.authorize(
					activity = context.asActivity(),
					doOnSuccess = {
						intentHandler.dispatch(LoginIntentHandler.LoginIntent.SignIn.Biometry)
					},
					doOnFailure = {},
				)
			}
		}

		MainContainer(mainState = state) {
			PinScreen(
				header = enterPinScreenHeader(
					onCloseClick = { intentHandler.pop() },
					onLogoutClick = {},
				),
				codeState = codeState,
				onInputCompleted = { pin ->
					intentHandler.dispatch(LoginIntentHandler.LoginIntent.SignIn.Pin(pin = pin))
				},
				actionCell = ActionCell.FingerPrint(
					onCellClick = {
						BiometryManager.authorize(
							activity = context.asActivity(),
							doOnSuccess = {
								intentHandler.dispatch(LoginIntentHandler.LoginIntent.SignIn.Biometry)
							},
							doOnFailure = {},
						)
					},
				)
			)
		}
	}

}