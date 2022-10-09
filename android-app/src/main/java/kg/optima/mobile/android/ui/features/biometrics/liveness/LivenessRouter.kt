package kg.optima.mobile.android.ui.features.biometrics.liveness

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.common.bankContacts.BankContactsScreen
import kg.optima.mobile.android.ui.features.registration.control_question.ControlQuestionScreen
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.registration.presentation.liveness.LivenessState

object LivenessRouter : FeatureRouter<LivenessState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: LivenessState.Model.NavigateTo): Screen {
		return when (stateModel) {
			LivenessState.Model.NavigateTo.Contacts -> BankContactsScreen
			is LivenessState.Model.NavigateTo.ControlQuestion ->
				ControlQuestionScreen(stateModel.hashCode)
			LivenessState.Model.NavigateTo.SelfConfirm -> SelfConfirmScreen
			LivenessState.Model.NavigateTo.Welcome -> WelcomeScreen
		}
	}
}