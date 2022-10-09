package kg.optima.mobile.android.ui.features.registration.agreement

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.common.offer.OfferScreen
import kg.optima.mobile.android.ui.features.registration.phone_number.PhoneNumberScreen
import kg.optima.mobile.registration.presentation.agreement.AgreementState

object AgreementRouter : FeatureRouter<AgreementState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: AgreementState.Model.NavigateTo): Screen {
		return when (stateModel) {
			AgreementState.Model.NavigateTo.RegistrationEnterPhone -> PhoneNumberScreen
			is AgreementState.Model.NavigateTo.RegistrationOfferta -> OfferScreen(stateModel.url)
		}
	}
}