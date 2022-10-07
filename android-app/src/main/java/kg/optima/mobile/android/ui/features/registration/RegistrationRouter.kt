package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementScreen
import kg.optima.mobile.base.presentation.UiState

object RegistrationRouter : FeatureRouter<UiState.Model.Navigate> {
    @Composable
    override fun compose(stateModel: UiState.Model.Navigate): Screen {
        return AgreementScreen/*when (stateModel) {
            RegistrationScreenModel.Agreement -> AgreementScreen
            is RegistrationScreenModel.Offerta -> OfferScreen(stateModel.url)
            RegistrationScreenModel.EnterPhone -> PhoneNumberScreen
            is RegistrationScreenModel.AcceptCode -> RegistrationOtpScreen(
                phoneNumber = stateModel.phoneNumber,
                timeLeft = stateModel.timeLeft,
                referenceId = stateModel.referenceId,
            )
            RegistrationScreenModel.SelfConfirm -> SelfConfirmScreen
            RegistrationScreenModel.ControlQuestion -> ControlQuestionScreen("hashCode")
            is RegistrationScreenModel.CreatePassword -> CreatePasswordScreen(
                hash = stateModel.hash,
                questionId = stateModel.questionId,
                answer = stateModel.answer
            )
        }*/
    }
}