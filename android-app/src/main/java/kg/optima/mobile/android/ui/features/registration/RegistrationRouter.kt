package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.biometrics.liveness.LivenessRouter
import kg.optima.mobile.android.ui.features.common.interview.InterviewRouter
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementRouter
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementScreen
import kg.optima.mobile.android.ui.features.registration.control_question.ControlQuestionRouter
import kg.optima.mobile.android.ui.features.registration.create_password.CreatePasswordRouter
import kg.optima.mobile.android.ui.features.registration.phone_number.PhoneNumberRouter
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmRouter
import kg.optima.mobile.android.ui.features.registration.sms_otp.RegistrationOtpRouter
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionState
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordState
import kg.optima.mobile.registration.presentation.interview.InterviewState
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState
import kg.optima.mobile.registration.presentation.sms_code.RegistrationSmsCodeState

object RegistrationRouter : FeatureRouter<RegistrationNavigateModel> {
	@Composable
	override fun compose(stateModel: RegistrationNavigateModel): Screen {
		return when (stateModel) {
			is AgreementState.Model.NavigateTo ->
				AgreementRouter.compose(stateModel = stateModel)
			is ControlQuestionState.Model.NavigateTo ->
				ControlQuestionRouter.compose(stateModel = stateModel)
			is CreatePasswordState.Model.NavigateTo ->
				CreatePasswordRouter.compose(stateModel = stateModel)
			is InterviewState.Model.NavigateTo ->
				InterviewRouter.compose(stateModel = stateModel)
			is LivenessState.Model.NavigateTo ->
				LivenessRouter.compose(stateModel)
			is PhoneNumberState.Model.NavigateTo ->
				PhoneNumberRouter.compose(stateModel = stateModel)
			is SelfConfirmState.Model.NavigateTo ->
				SelfConfirmRouter.compose(stateModel = stateModel)
			is RegistrationSmsCodeState.Model.NavigateTo ->
				RegistrationOtpRouter.compose(stateModel = stateModel)
			else -> AgreementScreen
		}
	}
}