package kg.optima.mobile.android.ui

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.launch.LaunchIntent
import kg.optima.mobile.common.presentation.launch.LaunchStateMachine
import org.koin.androidx.compose.inject


@Suppress("NAME_SHADOWING")
val startContent: @Composable (bottomSheetNavigator: BottomSheetNavigator) -> Unit = {
	val model = remember {
		CommonFeatureFactory.create<LaunchIntent, LaunchStateMachine>()
	}
	val stateMachine = model.stateMachine
	val intent = model.intent

	val router: Router by inject()

	val state by stateMachine.stateFlow.collectAsState(initial = StateMachine.State.Initial)

	val screens = remember { mutableStateOf(listOf<Screen>()) }

	when (val state = state) {
		is StateMachine.State.Initial ->
			intent.checkIsAuthorized()
		is StateMachine.State.Navigate ->
			screens.value = router.compose(screenModels = state.screenModels).map { it.screen }
	}

	if (screens.value.isNotEmpty()) {
		Navigator(screens = screens.value)
	}
}