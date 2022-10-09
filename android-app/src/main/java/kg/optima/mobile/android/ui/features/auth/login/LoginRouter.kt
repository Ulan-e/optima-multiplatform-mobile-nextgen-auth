package kg.optima.mobile.android.ui.features.auth.login

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.pin.enter.PinEnterScreen
import kg.optima.mobile.android.ui.features.auth.pin.set.PinSetScreen
import kg.optima.mobile.android.ui.features.auth.sms.AuthSmsCodeScreen
import kg.optima.mobile.auth.presentation.login.LoginState

object LoginRouter : FeatureRouter<LoginState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: LoginState.Model.NavigateTo): Screen {
		return when (stateModel) {
			is LoginState.Model.NavigateTo.ClientIdInfo -> TODO()
			LoginState.Model.NavigateTo.MainPage -> BottomNavigationScreen
			is LoginState.Model.NavigateTo.PinEnter -> PinEnterScreen
			LoginState.Model.NavigateTo.PinSet -> PinSetScreen
			is LoginState.Model.NavigateTo.SmsCode ->
				AuthSmsCodeScreen(stateModel.otpModel)
		}
	}

}