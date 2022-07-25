package kg.optima.mobile.auth.data.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserAuthenticationRequest(
    @SerialName("device_id")
    val deviceId: String,

    @SerialName("mobile")
    val mobile: String,

    @SerialName("password")
    val password: String
)