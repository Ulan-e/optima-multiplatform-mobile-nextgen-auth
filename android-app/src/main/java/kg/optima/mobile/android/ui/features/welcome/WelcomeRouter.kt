package kg.optima.mobile.android.ui.features.welcome

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.auth.login.LoginScreen
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementScreen
import kg.optima.mobile.common.presentation.welcome.WelcomeState

object WelcomeRouter : FeatureRouter<WelcomeState.Model> {
	@Composable
	override fun compose(stateModel: WelcomeState.Model): Screen {
		return when (stateModel) {
			WelcomeState.Model.NavigateTo.Contacts -> TODO()
			WelcomeState.Model.NavigateTo.Languages -> TODO()
			WelcomeState.Model.NavigateTo.Login -> LoginScreen
			WelcomeState.Model.NavigateTo.Map -> TODO()
			WelcomeState.Model.NavigateTo.Rates -> TODO()
			WelcomeState.Model.NavigateTo.RegisterAgreement -> AgreementScreen
		}
	}

}