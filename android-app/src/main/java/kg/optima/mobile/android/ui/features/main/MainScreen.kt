package kg.optima.mobile.android.ui.features.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object MainScreen : Screen {
	@Composable
	override fun Content() {
		Text(text = "Main Page")
	}
}