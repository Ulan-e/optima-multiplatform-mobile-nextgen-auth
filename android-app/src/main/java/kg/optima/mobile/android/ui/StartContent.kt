package kg.optima.mobile.android.ui

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.android.ui.base.routing.Router
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.launch.LaunchIntent
import kg.optima.mobile.common.presentation.launch.LaunchState
import kg.optima.mobile.core.navigation.ScreenModel
import org.koin.androidx.compose.inject

@Suppress("NAME_SHADOWING")
@Composable
fun StartContent(screenModel: ScreenModel? = null) {
	val product = remember {
		CommonFeatureFactory.create<LaunchIntent, LaunchState>()
	}
	val state = product.state
	val intent = remember {
		product.intent.also { if (screenModel != null) it.nextScreenModel(screenModel) }
	}

	val router: Router by inject()

	val model by state.stateFlow.collectAsState(initial = BaseMppState.StateModel.Initial)

	val screens = remember { mutableStateOf(listOf<Screen>()) }

	when (val state = model) {
		is BaseMppState.StateModel.Initial ->
			intent.checkIsAuthorized()
		is BaseMppState.StateModel.Navigate ->
			screens.value = router.compose(screenModels = state.screenModels).map { it.screen }
	}

	if (screens.value.isNotEmpty()) {
		Navigator(screens = screens.value)
	}
}