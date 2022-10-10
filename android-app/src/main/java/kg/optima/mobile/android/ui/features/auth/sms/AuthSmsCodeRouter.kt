package kg.optima.mobile.android.ui.features.auth.sms

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.pin.enter.PinEnterScreen
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeState

object AuthSmsCodeRouter : FeatureRouter<AuthSmsCodeState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: AuthSmsCodeState.Model.NavigateTo): Screen {
		return when (stateModel) {
			AuthSmsCodeState.Model.NavigateTo.Main -> BottomNavigationScreen
			AuthSmsCodeState.Model.NavigateTo.PinEnter -> PinEnterScreen
		}
	}
}