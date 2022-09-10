package kg.optima.mobile.android.ui.features.menu

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.navigation.root.MenuComponent

@Composable
fun MenuContent(historyComponent: MenuComponent) {
	val screens = mutableListOf<Screen>()

	Navigator(screens = screens)
}