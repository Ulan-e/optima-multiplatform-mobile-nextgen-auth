package kg.optima.mobile.android.ui.features.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.common.bankContacts.BankContactsScreen
import kg.optima.mobile.base.presentation.UiState

object CommonRouter : FeatureRouter<UiState.Model.Navigate> {
	@Composable
	override fun compose(stateModel: UiState.Model.Navigate): Screen {
		return BankContactsScreen/*when (stateModel) {
			CommonScreenModel.BankContacts -> BankContactsScreen
			is CommonScreenModel.Interview -> InterviewScreen(stateModel.url)
			is CommonScreenModel.SmsCode -> SmsCodeScreen(otpModel = stateModel.otpModel)
		}*/
	}

}