package kg.optima.mobile.android.ui.features.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.navigation.root.MainComponent

class MainScreen(
	private val component: MainComponent? = null,
) : BaseScreen {
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