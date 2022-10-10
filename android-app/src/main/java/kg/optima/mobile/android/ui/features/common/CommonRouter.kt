package kg.optima.mobile.android.ui.features.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.welcome.WelcomeRouter
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.welcome.WelcomeState

object CommonRouter : FeatureRouter<UiState.Model.Navigate> {
	@Composable
	override fun compose(stateModel: UiState.Model.Navigate): Screen {
		return when (stateModel) {
			is WelcomeState.Model.NavigateTo -> WelcomeRouter.compose(stateModel = stateModel)
			else -> BottomNavigationScreen
		}
	}

}