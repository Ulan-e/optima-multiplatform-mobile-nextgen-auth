package kg.optima.mobile.auth.data.api.model.resetPassword

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PasswordResetRequest(
    @SerialName("password")
    val password: String
)