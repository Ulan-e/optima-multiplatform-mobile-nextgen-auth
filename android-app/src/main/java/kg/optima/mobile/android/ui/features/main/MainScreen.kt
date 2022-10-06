package kg.optima.mobile.android.ui.features.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer

@Parcelize
class MainScreen(
//	@IgnoredOnParcel private val component: MainComponent? = null,
) : BaseScreen {
	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		MainContainer(
			mainState = null,
//			component = component,
		) {
			Text(text = "Main Page")
		}
	}
}