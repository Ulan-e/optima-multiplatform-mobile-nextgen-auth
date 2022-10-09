package kg.optima.mobile.android.ui.features.auth

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.auth.login.LoginScreen
import kg.optima.mobile.base.presentation.UiState

object AuthRouter : FeatureRouter<UiState.Model.Navigate> {
	@Composable
	override fun compose(stateModel: UiState.Model.Navigate): Screen {
		return LoginScreen/*when (stateModel) {
			is AuthScreenModel.Login -> LoginScreen(stateModel.nextScreenModel)
			is AuthScreenModel.PinEnter ->
				PinEnterScreen(stateModel.showBiometry, stateModel.nextScreenModel)
			is AuthScreenModel.PinSet -> PinSetScreen(stateModel.nextScreenModel)
			is AuthScreenModel.SmsCode ->
				AuthSmsCodeScreen(stateModel.otpModel, stateModel.nextScreenModel)
		}*/
	}

}