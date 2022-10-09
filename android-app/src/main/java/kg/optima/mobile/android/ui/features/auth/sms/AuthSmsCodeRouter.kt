package kg.optima.mobile.android.ui.features.auth.sms

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.auth.pin.enter.PinEnterScreen
import kg.optima.mobile.android.ui.features.optima24.Optima24Model
import kg.optima.mobile.android.ui.features.optima24.Optima24Screen
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeState

object AuthSmsCodeRouter : FeatureRouter<AuthSmsCodeState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: AuthSmsCodeState.Model.NavigateTo): Screen {
		return when (stateModel) {
			AuthSmsCodeState.Model.NavigateTo.Main -> Optima24Screen(Optima24Model.MainPage)
			AuthSmsCodeState.Model.NavigateTo.PinEnter -> PinEnterScreen
		}
	}
}