package kg.optima.mobile.android.ui.features.registration.create_password

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.common.interview.InterviewScreen
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordState

object CreatePasswordRouter : FeatureRouter<CreatePasswordState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: CreatePasswordState.Model.NavigateTo): Screen {
		return when (stateModel) {
			is CreatePasswordState.Model.NavigateTo.RegistrationInterview ->
				InterviewScreen(stateModel.url)
			CreatePasswordState.Model.NavigateTo.Welcome -> WelcomeScreen
		}
	}
}