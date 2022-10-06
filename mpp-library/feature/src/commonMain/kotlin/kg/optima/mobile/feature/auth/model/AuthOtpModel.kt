package kg.optima.mobile.feature.auth.model

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.feature.common.OtpModel

@Parcelize
class AuthOtpModel(
	override val phoneNumber: String,
	private val signInInfo: SignInInfo,
) : OtpModel<SignInInfo>(phoneNumber, signInInfo)

@Parcelize
data class SignInInfo(
	val clientId: String,
	val password: String,
	val smsCode: String? = null,
) : Parcelable