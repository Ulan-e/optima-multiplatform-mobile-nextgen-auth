package kg.optima.mobile.android.ui.features.auth.pin

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager


class PinEnterScreen(
	private val showBiometry: Boolean = false,
	private val nextScreenModel: ScreenModel,
) : Screen {

	@Composable
	override fun Content() {
		val model = remember {
			AuthFeatureFactory.create<LoginIntent, LoginStateMachine>(nextScreenModel)
		}
		val stateMachine = model.stateMachine
		val intent = model.intent

		val state by stateMachine.stateFlow.collectAsState(
			initial = if (showBiometry) LoginStateMachine.LoginState.ShowBiometry else null
		)

		val context = LocalContext.current

		val codeState = remember { mutableStateOf(emptyString) }

		val onBiometryAuthenticateSuccess: () -> Unit = {
			intent.signIn(LoginIntent.SignInInfo.Biometry)
		}
		val onBiometryAuthenticate: () -> Unit = {
			BiometryManager.authorize(
				activity = context.asActivity(),
				doOnSuccess = onBiometryAuthenticateSuccess,
				doOnFailure = {},
			)
		}

		when (state) {
			is LoginStateMachine.LoginState.ShowBiometry -> onBiometryAuthenticate()
		}

		MainContainer(mainState = state) {
			PinScreen(
				header = enterPinScreenHeader(
					onCloseClick = { intent.pop() },
					onLogoutClick = {},
				),
				codeState = codeState,
				onInputCompleted = { pin ->
					intent.signIn(LoginIntent.SignInInfo.Pin(pin = pin))
				},
				actionCell = ActionCell.FingerPrint(
					onCellClick = { onBiometryAuthenticate() },
				)
			)
		}
	}

}