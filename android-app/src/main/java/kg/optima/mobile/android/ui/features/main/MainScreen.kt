package kg.optima.mobile.android.ui.features.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.navigation.root.MainComponent

class MainScreen(
	private val component: MainComponent? = null,
) : Screen {
	@Composable
	override fun Content() {
		MainContainer(
			mainState = null,
			component = component,
		) {
			Text(text = "Main Page")
		}
	}
}