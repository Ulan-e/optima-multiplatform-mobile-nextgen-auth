package kg.optima.mobile.auth.data.api.model.otp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthorizeOtpRequest(
    @SerialName("mobile")
    val mobile: String,

    @SerialName("otp")
    val otp: String
)