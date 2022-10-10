package kg.optima.mobile.android.ui.features.registration.phone_number

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.sms_otp.RegistrationOtpModel
import kg.optima.mobile.android.ui.features.registration.sms_otp.RegistrationOtpScreen
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState

object PhoneNumberRouter : FeatureRouter<PhoneNumberState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: PhoneNumberState.Model.NavigateTo): Screen {
		return when (stateModel) {
			is PhoneNumberState.Model.NavigateTo.RegistrationSmsCode ->
				RegistrationOtpScreen(
					registrationOtpModel = RegistrationOtpModel(
						phoneNumber = stateModel.phoneNumber,
						timeLeft = stateModel.timeLeft,
						referenceId = stateModel.referenceId
					)
				)
			PhoneNumberState.Model.NavigateTo.Welcome -> WelcomeScreen
		}
	}
}