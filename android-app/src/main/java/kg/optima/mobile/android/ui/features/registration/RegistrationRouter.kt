package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.phone_number.PhoneNumberScreen
import kg.optima.mobile.android.ui.features.registration.secret_question.ControlQuestionScreen
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.android.ui.features.registration.sms_otp.SmsOtpScreen
import kg.optima.mobile.feature.register.RegistrationScreenModel

object RegistrationRouter : FeatureRouter<RegistrationScreenModel> {
	@Composable
	override fun compose(screenModel: RegistrationScreenModel): Screen {
		return when (screenModel) {
			RegistrationScreenModel.Agreement -> ControlQuestionScreen("hashCode")
			RegistrationScreenModel.EnterPhone -> PhoneNumberScreen
			is RegistrationScreenModel.AcceptCode -> SmsOtpScreen(
				phoneNumber = screenModel.phoneNumber,
				timeout = screenModel.timeout,
				referenceId = screenModel.referenceId,
			)
			RegistrationScreenModel.SelfConfirm -> SelfConfirmScreen
			RegistrationScreenModel.ControlQuestion -> ControlQuestionScreen("hashCode")

			is RegistrationScreenModel.Password -> PasswordScreen(
				hashCode = screenModel.hashCode,
				questionId = screenModel.questionId,
				answer = screenModel.answer
			)
			//TODO: add arguments
		}
	}
}