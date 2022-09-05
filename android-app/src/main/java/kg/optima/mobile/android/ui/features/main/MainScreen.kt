package kg.optima.mobile.android.ui.features.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer

object MainScreen : Screen {
	@Composable
	override fun Content() {
		MainContainer(mainState = null) {
			Text(text = "Main Page")
		}
	}
}