package kg.optima.mobile.feature.auth

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.model.AuthOtpModel
import kg.optima.mobile.feature.common.OtpModel

sealed interface AuthScreenModel : ScreenModel {
	val nextScreenModel: ScreenModel

	@Parcelize
	class Login(override val nextScreenModel: ScreenModel) : AuthScreenModel

	@Parcelize
	class PinEnter(
		val showBiometry: Boolean,
		override val nextScreenModel: ScreenModel,
	) : AuthScreenModel

	@Parcelize
	class PinSet(override val nextScreenModel: ScreenModel) : AuthScreenModel {
		override val dropBackStack: Boolean = true
	}

	@Parcelize
	class SmsCode(
		override val nextScreenModel: ScreenModel,
		val otpModel: AuthOtpModel,
	) : AuthScreenModel
}