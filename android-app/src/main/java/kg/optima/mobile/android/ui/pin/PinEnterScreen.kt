package kg.optima.mobile.android.ui.pin

import android.util.Log
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.main.MainScreen
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.login.LoginFactory
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager

class PinEnterScreen(
	private val clientId: String,
	private val activity: FragmentActivity,
) : Screen {
	@Composable
	override fun Content() {
		val stateMachine: LoginStateMachine = LoginFactory.stateMachine
		val intentHandler: LoginIntentHandler = LoginFactory.intentHandler

		val state by stateMachine.state.collectAsState(initial = null)

		val navigator = LocalNavigator.currentOrThrow

		val codeState = remember { mutableStateOf(emptyString) }

		when (state) {
			is LoginStateMachine.LoginState.SignIn ->
				navigator.push(MainScreen)
			is StateMachine.State.Loading ->
				Log.d("PinEnterScreen", "Loading State")
			is StateMachine.State.Error ->
				Log.d("PinEnterScreen", "Error State")
		}

		PinScreen(
			header = enterPinScreenHeader(
				onCloseClick = { navigator.pop() },
				onLogoutClick = {

				},
			),
			codeState = codeState,
			onInputCompleted = {
				intentHandler.dispatch(
					LoginIntentHandler.LoginIntent.SignIn(
						clientId = clientId,
						password = "",
						grantType = GrantType.Password
					)
				)
			},
			actionCell = ActionCell.FingerPrint(
				onCellClick = {
					BiometryManager.authorize(
						activity = activity,
						doOnSuccess = {

						},
						doOnFailure = {

						},
					)
				},
			)
		)
	}
}