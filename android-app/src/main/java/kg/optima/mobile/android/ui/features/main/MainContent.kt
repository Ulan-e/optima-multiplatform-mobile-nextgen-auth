package kg.optima.mobile.android.ui.features.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.navigation.root.MainComponent

@Composable
fun MainContent(mainComponent: MainComponent) {
	val screens = mutableListOf<Screen>()

	mainComponent.items.filterIsInstance<UiState.Model.Navigate>().forEach {
		val screen = when (it) {
			else -> MainScreen(/*mainComponent*/)
		}
		screens.add(screen)
	}

	if (screens.isNotEmpty()) {
		Navigator(screens = screens)
	}
}