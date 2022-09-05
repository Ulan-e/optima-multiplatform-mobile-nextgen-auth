package kg.optima.mobile.android.ui

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.common.presentation.launch.LaunchIntentFactory
import kg.optima.mobile.common.presentation.launch.LaunchIntentHandler
import kg.optima.mobile.common.presentation.launch.LaunchStateMachine
import org.koin.androidx.compose.inject


@Suppress("NAME_SHADOWING")
val startContent: @Composable (bottomSheetNavigator: BottomSheetNavigator) -> Unit = {
	val stateMachine: LaunchStateMachine = LaunchIntentFactory.stateMachine
	val intentHandler: LaunchIntentHandler = LaunchIntentFactory.intentHandler

	val router: Router by inject()

	val state by stateMachine.state.collectAsState(initial = StateMachine.State.Initial)

	val screens = remember { mutableStateOf(listOf<Screen>()) }

	when (val state = state) {
		is StateMachine.State.Initial ->
			intentHandler.dispatch(LaunchIntentHandler.LaunchIntent.CheckIsAuthorized)
		is StateMachine.State.Navigate ->
			screens.value = router.compose(screenModels = state.screenModels).map { it.screen }
	}

	if (screens.value.isNotEmpty()) {
		Navigator(screens = screens.value)
	}
}