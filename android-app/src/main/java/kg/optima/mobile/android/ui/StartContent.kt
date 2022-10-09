package kg.optima.mobile.android.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.android.ui.base.routing.Router
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.base.presentation.UiState
import org.koin.androidx.compose.inject

@Composable
fun StartContent(stateModel: UiState.Model.Navigate? = null) {
	val router: Router by inject()

	val screens = mutableListOf<Screen>(WelcomeScreen)
	if (stateModel != null)
		screens.add(router.compose(stateModel = stateModel).screen)

	Navigator(screens = screens)
}