package kg.optima.mobile.auth.data.api.model.resetPassword

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PasswordResetResponse(
    @SerialName("msg")
    val msg: String? = null
)