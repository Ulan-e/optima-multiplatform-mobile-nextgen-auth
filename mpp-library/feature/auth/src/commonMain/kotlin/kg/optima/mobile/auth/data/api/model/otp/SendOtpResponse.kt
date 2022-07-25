package kg.optima.mobile.auth.data.api.model.otp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SendOtpResponse(
    @SerialName("is_sent")
    val isSent: Boolean? = null,

    @SerialName("otp_resend_time")
    val otpResendTime: String? = null
)