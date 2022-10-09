package kg.optima.mobile.android.ui.features.auth

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.login.LoginRouter
import kg.optima.mobile.android.ui.features.auth.pin.enter.PinEnterRouter
import kg.optima.mobile.android.ui.features.auth.pin.set.PinSetRouter
import kg.optima.mobile.android.ui.features.auth.sms.AuthSmsCodeRouter
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.auth.presentation.pin_enter.PinEnterState
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthState
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeState

object AuthRouter : FeatureRouter<AuthNavigateModel> {
	@Composable
	override fun compose(stateModel: AuthNavigateModel): Screen {
		return when (stateModel) {
			is LoginState.Model.NavigateTo ->
				LoginRouter.compose(stateModel = stateModel)
			is AuthSmsCodeState.Model.NavigateTo ->
				AuthSmsCodeRouter.compose(stateModel = stateModel)
			is SetupAuthState.Model.NavigateTo ->
				PinSetRouter.compose(stateModel = stateModel)
			is PinEnterState.Model.NavigateTo ->
				PinEnterRouter.compose(stateModel = stateModel)
			else -> BottomNavigationScreen
		}
	}
}