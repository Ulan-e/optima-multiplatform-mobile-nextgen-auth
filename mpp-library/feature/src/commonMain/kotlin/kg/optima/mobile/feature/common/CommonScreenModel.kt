package kg.optima.mobile.feature.common

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface CommonScreenModel : ScreenModel {
	@Parcelize
	object BankContacts: CommonScreenModel

	@Parcelize
	class Interview(
		val url: String
	) : CommonScreenModel

	@Parcelize
	class SmsCode(val otpModel: OtpModel<*>) : CommonScreenModel
}