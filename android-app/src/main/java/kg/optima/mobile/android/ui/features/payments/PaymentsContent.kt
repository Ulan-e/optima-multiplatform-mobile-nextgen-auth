package kg.optima.mobile.android.ui.features.payments

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kg.optima.mobile.navigation.root.PaymentsComponent

@Composable
fun PaymentsContent(paymentsComponent: PaymentsComponent) {
	val screens = mutableListOf<Screen>()

	Navigator(screens = screens)
}