package kg.optima.mobile.android.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.launch.LaunchIntent
import kg.optima.mobile.common.presentation.launch.LaunchState
import org.koin.androidx.compose.inject


@OptIn(ExperimentalAnimationApi::class)
@Suppress("NAME_SHADOWING")
val startContent: @Composable (bottomSheetNavigator: BottomSheetNavigator) -> Unit = {
	val product = remember {
		CommonFeatureFactory.create<LaunchIntent, LaunchState>()
	}
	val state = product.state
	val intent = product.intent

	val router: Router by inject()

	val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

	val screens = remember { mutableStateOf(listOf<Screen>()) }

	when (val state = model) {
		is State.StateModel.Initial ->
			intent.checkIsAuthorized()
		is State.StateModel.Navigate ->
			screens.value = router.compose(screenModels = state.screenModels).map { it.screen }
	}

	if (screens.value.isNotEmpty()) {
		Navigator(screens = screens.value) {
			SlideTransition(navigator = it)
		}
	}
}