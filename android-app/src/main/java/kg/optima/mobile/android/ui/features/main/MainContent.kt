package kg.optima.mobile.android.ui.features.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.feature.main.MainScreenModel
import kg.optima.mobile.navigation.root.MainComponent

@Composable
fun MainContent(mainComponent: MainComponent) {
	val screens = mutableListOf<Screen>()

	mainComponent.items.filterIsInstance<MainScreenModel>().forEach {
		val screen = when (it) {
			is MainScreenModel.Main -> MainScreen(mainComponent)
		}
		screens.add(screen)
	}

	if (screens.isNotEmpty()) {
		Navigator(screens = screens)
	}
}