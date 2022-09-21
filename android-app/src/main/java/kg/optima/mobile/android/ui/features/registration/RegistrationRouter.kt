package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.common.offer.OfferScreen
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementScreen
import kg.optima.mobile.android.ui.features.registration.control_question.ControlQuestionScreen
import kg.optima.mobile.android.ui.features.registration.create_password.CreatePasswordScreen
import kg.optima.mobile.android.ui.features.registration.phone_number.PhoneNumberScreen
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.android.ui.features.registration.sms_otp.OtpScreen
import kg.optima.mobile.feature.registration.RegistrationScreenModel

object RegistrationRouter : FeatureRouter<RegistrationScreenModel> {
	@Composable
	override fun compose(screenModel: RegistrationScreenModel): Screen {
		return when (screenModel) {
			RegistrationScreenModel.Agreement -> AgreementScreen
			is RegistrationScreenModel.Offerta -> OfferScreen(screenModel.url)
			RegistrationScreenModel.EnterPhone -> PhoneNumberScreen
			is RegistrationScreenModel.AcceptCode -> OtpScreen(
				phoneNumber = screenModel.phoneNumber,
				timeLeft = screenModel.timeLeft,
				referenceId = screenModel.referenceId,
			)
			RegistrationScreenModel.SelfConfirm -> SelfConfirmScreen
			RegistrationScreenModel.ControlQuestion -> ControlQuestionScreen("hashCode")
			is RegistrationScreenModel.CreatePassword -> CreatePasswordScreen(
				hash = screenModel.hash,
				questionId = screenModel.questionId,
				answer = screenModel.answer
			)
		}
	}
}