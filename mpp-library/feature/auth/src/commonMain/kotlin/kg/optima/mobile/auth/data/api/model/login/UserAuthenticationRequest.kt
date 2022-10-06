package kg.optima.mobile.auth.data.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserAuthenticationRequest(
	@SerialName("login")
	val clientId: String,

	@SerialName("hashpassword")
	val password: String,

	@SerialName("confirmationcode")
	val smsCode: String?
)