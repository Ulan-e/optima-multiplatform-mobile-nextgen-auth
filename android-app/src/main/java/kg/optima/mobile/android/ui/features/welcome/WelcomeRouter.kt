package kg.optima.mobile.android.ui.features.welcome

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

object WelcomeRouter : FeatureRouter<WelcomeScreenModel> {
	@Composable
	override fun compose(screenModel: WelcomeScreenModel): Screen {
		return when (screenModel) {
			WelcomeScreenModel.Welcome -> WelcomeScreen
		}
	}

}