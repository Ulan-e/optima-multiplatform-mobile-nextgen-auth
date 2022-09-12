package kg.optima.mobile.android.ui.features.registration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.feature.register.RegistrationScreenModel

object RegistrationRouter : FeatureRouter<RegistrationScreenModel> {
	@Composable
	override fun compose(screenModel: RegistrationScreenModel): Screen {
		return when (screenModel) {
			RegistrationScreenModel.Agreement -> AgreementScreen
			RegistrationScreenModel.EnterPhone -> PhoneNumberScreen
			is RegistrationScreenModel.AcceptCode -> AgreementScreen
		}
	}
}