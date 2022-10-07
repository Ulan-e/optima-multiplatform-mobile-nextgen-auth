package kg.optima.mobile.android.ui.features.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.common.bankContacts.BankContactsScreen
import kg.optima.mobile.android.ui.features.common.interview.InterviewScreen
import kg.optima.mobile.android.ui.features.common.otp.SmsCodeScreen
import kg.optima.mobile.feature.common.CommonScreenModel

object CommonRouter : FeatureRouter<CommonScreenModel> {

	@Composable
	override fun compose(screenModel: CommonScreenModel): Screen {
		return when (screenModel) {
			CommonScreenModel.BankContacts -> BankContactsScreen
			is CommonScreenModel.Interview -> InterviewScreen(screenModel.url)
			is CommonScreenModel.SmsCode -> SmsCodeScreen(otpModel = screenModel.otpModel)
		}
	}

}