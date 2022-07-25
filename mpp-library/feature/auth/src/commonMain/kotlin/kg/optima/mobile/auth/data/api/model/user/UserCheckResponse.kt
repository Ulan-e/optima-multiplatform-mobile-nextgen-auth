package kg.optima.mobile.auth.data.api.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserCheckResponse(
    @SerialName("is_verified")
    val isVerified: Boolean? = null,

    @SerialName("otp_resend_time")
    val otpResendTime: String? = null
)