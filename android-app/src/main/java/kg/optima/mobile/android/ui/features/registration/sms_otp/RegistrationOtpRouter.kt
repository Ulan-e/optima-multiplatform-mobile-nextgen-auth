package kg.optima.mobile.android.ui.features.registration.sms_otp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.registration.presentation.sms_code.RegistrationSmsCodeState

object RegistrationOtpRouter : FeatureRouter<RegistrationSmsCodeState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: RegistrationSmsCodeState.Model.NavigateTo): Screen {
		return when (stateModel) {
			RegistrationSmsCodeState.Model.NavigateTo.SelfConfirm -> SelfConfirmScreen
		}
	}
}