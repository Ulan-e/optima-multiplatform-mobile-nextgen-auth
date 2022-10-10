package kg.optima.mobile.android.ui.features.welcome

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.auth.login.LoginScreen
import kg.optima.mobile.android.ui.features.common.bankContacts.BankContactsScreen
import kg.optima.mobile.android.ui.features.optima24.Optima24Model
import kg.optima.mobile.android.ui.features.optima24.Optima24Screen
import kg.optima.mobile.android.ui.features.registration.agreement.AgreementScreen
import kg.optima.mobile.common.presentation.welcome.WelcomeState

object WelcomeRouter : FeatureRouter<WelcomeState.Model> {
	@Composable
	override fun compose(stateModel: WelcomeState.Model): Screen {
		return when (stateModel) {
			WelcomeState.Model.NavigateTo.Contacts -> BankContactsScreen
			WelcomeState.Model.NavigateTo.Languages -> Optima24Screen(Optima24Model.Language)
			WelcomeState.Model.NavigateTo.Login -> LoginScreen
			WelcomeState.Model.NavigateTo.Map -> Optima24Screen(Optima24Model.OnMap)
			WelcomeState.Model.NavigateTo.Rates -> Optima24Screen(Optima24Model.Rates)
			WelcomeState.Model.NavigateTo.RegisterAgreement -> AgreementScreen
		}
	}

}