package kg.optima.mobile.auth.data.api.model.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserRegistrationRequest(
    @SerialName("device_id")
    val deviceId: String,

    @SerialName("password")
    val password: String
)