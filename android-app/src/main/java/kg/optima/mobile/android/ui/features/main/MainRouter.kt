package kg.optima.mobile.android.ui.features.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.main.MainScreenModel

object MainRouter : FeatureRouter<MainScreenModel> {

	@Composable
	override fun compose(screenModel: MainScreenModel): Screen {
		return when (screenModel) {
			MainScreenModel.Main -> MainScreen()
		}
	}

	@Composable
	fun default(screenModel: ScreenModel): Screen {
		// TODO send screenModel to analytics
		return BottomNavigationScreen
	}

}