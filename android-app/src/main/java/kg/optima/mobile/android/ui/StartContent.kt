package kg.optima.mobile.android.ui

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kg.optima.mobile.android.ui.login.LoginScreen
import kg.optima.mobile.android.ui.pin.PinEnterScreen
import kg.optima.mobile.android.ui.welcome.WelcomeScreen
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.auth_state.AuthStateFactory
import kg.optima.mobile.auth.presentation.auth_state.AuthStateIntentHandler
import kg.optima.mobile.auth.presentation.auth_state.AuthStatusStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager


@Suppress("NAME_SHADOWING")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(activity: FragmentActivity) {
	val stateMachine: AuthStatusStateMachine = AuthStateFactory.stateMachine
	val intentHandler: AuthStateIntentHandler = AuthStateFactory.intentHandler
	val state by stateMachine.state.collectAsState(initial = null)

	val screens = remember {
		intentHandler.dispatch(AuthStateIntentHandler.CheckIsAuthorizedIntent)
		mutableStateOf<List<Screen>>(listOf())
	}

	when (val state = state) {
		is AuthStatusStateMachine.AuthStatusState -> {
			val items = mutableListOf(
				WelcomeScreen(activity),
				LoginScreen(state.clientId)
			)
			when (state) {
				is AuthStatusStateMachine.AuthStatusState.Authorized -> {
					if (state.grantTypes.contains(GrantType.Pin)) {
						items.add(PinEnterScreen(state.clientId, activity))
					}
					if (state.grantTypes.contains(GrantType.Biometry)) {
						BiometryManager.authorize(
							activity = activity,
							doOnSuccess = {

							},
							doOnFailure = {

							},
						)
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

	BottomSheetNavigator(
		sheetElevation = 0.dp,
		sheetBackgroundColor = Color.Transparent,
		sheetShape = RoundedCornerShape(16.dp, 16.dp),
		content = {
			if (screens.value.isNotEmpty()) {
				Navigator(screens = screens.value)
			}
		},
	)
}