package kg.optima.mobile.android.ui.features.auth.pin.set

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthState

object PinSetRouter : FeatureRouter<SetupAuthState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: SetupAuthState.Model.NavigateTo): Screen {
		return when (stateModel) {
			SetupAuthState.Model.NavigateTo.Main -> BottomNavigationScreen
		}
	}
}