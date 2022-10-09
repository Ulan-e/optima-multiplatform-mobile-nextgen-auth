package kg.optima.mobile.android.ui.features.auth.pin.enter

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.auth.presentation.pin_enter.PinEnterState

object PinEnterRouter : FeatureRouter<PinEnterState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: PinEnterState.Model.NavigateTo): Screen {
		return when (stateModel) {
			PinEnterState.Model.NavigateTo.Main -> BottomNavigationScreen
		}
	}
}