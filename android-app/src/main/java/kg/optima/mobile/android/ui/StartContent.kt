package kg.optima.mobile.android.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.android.ui.auth.AuthScreens
import kg.optima.mobile.android.ui.welcome.EntryScreens
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.auth_state.AuthStateFactory
import kg.optima.mobile.auth.presentation.auth_state.AuthStateIntentHandler
import kg.optima.mobile.auth.presentation.auth_state.AuthStatusStateMachine
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.presentation.StateMachine


@Suppress("NAME_SHADOWING")
@Composable
fun StartContent() {
	val stateMachine: AuthStatusStateMachine = AuthStateFactory.stateMachine
	val intentHandler: AuthStateIntentHandler = AuthStateFactory.intentHandler
	val state by stateMachine.state.collectAsState(initial = null)

	val screens = remember {
		intentHandler.dispatch(AuthStateIntentHandler.AuthStateIntent.CheckIsAuthorized)
		mutableStateOf<List<Screen>>(listOf())
	}

	val welcomeScreen = rememberScreen(provider = EntryScreens.Welcome)
	val loginScreen = rememberScreen(provider = AuthScreens.Login)

	@Composable
	fun pinEnterScreen(showBiometry: Boolean) =
		rememberScreen(provider = AuthScreens.PinEnter(showBiometry))

	when (val state = state) {
		is AuthStatusStateMachine.AuthStatusState -> {
			val items = mutableListOf(welcomeScreen)
			when (state) {
				is AuthStatusStateMachine.AuthStatusState.Authorized -> {
					items.add(loginScreen)
					if (state.grantTypes.contains(GrantType.Pin)) {
						items.add(pinEnterScreen(state.grantTypes.contains(GrantType.Biometry)))
					}
				}
				is AuthStatusStateMachine.AuthStatusState.NotAuthorized -> Unit
			}
			screens.value = items
		}
		is StateMachine.State.Loading ->
			Log.d("MainScreen", "Loading State")
		is StateMachine.State.Error ->
			Log.d("MainScreen", "Error State")
		null -> Unit
	}

	if (screens.value.isNotEmpty()) {
		Navigator(screens = screens.value)
	}
}