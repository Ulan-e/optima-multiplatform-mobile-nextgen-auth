package kg.optima.mobile.android.ui.features.transfers

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.navigation.root.TransfersComponent

@Composable
fun TransfersContent(transfersComponent: TransfersComponent) {
	val screens = mutableListOf<Screen>()

	Navigator(screens = screens)
}