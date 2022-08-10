package kg.optima.mobile.android.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.android.ui.login.LoginScreen
import kg.optima.mobile.android.ui.welcome.WelcomeScreen
import kg.optima.mobile.auth.presentation.auth_state.AuthStateFactory
import kg.optima.mobile.auth.presentation.auth_state.AuthStateIntentHandler
import kg.optima.mobile.auth.presentation.auth_state.AuthStatusStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		launch()
	}

	private fun launch() {
		lifecycleScope.launchWhenResumed {
			setContent {
				ProvideWindowInsets {
					Theme.OptimaTheme(content = content)
				}
			}
		}
	}

	@Suppress("NAME_SHADOWING")
	@OptIn(ExperimentalMaterialApi::class)
	private val content: @Composable () -> Unit = {
		val stateMachine: AuthStatusStateMachine = AuthStateFactory.stateMachine
		val intentHandler: AuthStateIntentHandler = AuthStateFactory.intentHandler
		val state by stateMachine.state.collectAsState(initial = null)

		val screens = remember {
			intentHandler.dispatch(AuthStateIntentHandler.CheckIsAuthorizedIntent)
			mutableStateOf<List<Screen>>(listOf(WelcomeScreen))
		}

		when (val state = state) {
			is AuthStatusStateMachine.AuthStatusState.ClientInfo -> {
				if (state.isAuthorized) screens.value = screens.value + LoginScreen(state.clientId)
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
				Navigator(screens = screens.value)
			},
		)
	}
}
