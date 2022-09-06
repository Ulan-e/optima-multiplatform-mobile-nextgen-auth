package kg.optima.mobile.android.ui.features.history

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.navigation.root.HistoryComponent

@Composable
fun HistoryContent(historyComponent: HistoryComponent) {
	val screens = mutableListOf<Screen>()

	Navigator(screens = screens)
}