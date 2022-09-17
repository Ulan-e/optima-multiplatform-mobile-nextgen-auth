package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.create_password.CreatePasswordScreen
import kg.optima.mobile.android.ui.features.registration.phone_number.PhoneNumberScreen
import kg.optima.mobile.android.ui.features.registration.control_question.ControlQuestionScreen
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.android.ui.features.registration.sms_otp.SmsCodeScreen
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.feature.registration.RegistrationScreenModel

object RegistrationRouter : FeatureRouter<RegistrationScreenModel> {
	@Composable
	override fun compose(screenModel: RegistrationScreenModel): Screen {
		return when (screenModel) {
			RegistrationScreenModel.Agreement -> AgreementScreen
			RegistrationScreenModel.EnterPhone -> PhoneNumberScreen
			is RegistrationScreenModel.AcceptCode -> SmsCodeScreen(
				phoneNumber = screenModel.phoneNumber,
				timeout = screenModel.timeout,
				referenceId = screenModel.referenceId,
			)
			RegistrationScreenModel.SelfConfirm -> SelfConfirmScreen
			RegistrationScreenModel.ControlQuestion -> ControlQuestionScreen("hashCode")
			RegistrationScreenModel.ControlQuestion -> ControlQuestionScreen(emptyString)
			is RegistrationScreenModel.CreatePassword -> CreatePasswordScreen(
				hash = screenModel.hash,
				questionId = screenModel.questionId,
				answer = screenModel.answer
			)
		}
	}
}