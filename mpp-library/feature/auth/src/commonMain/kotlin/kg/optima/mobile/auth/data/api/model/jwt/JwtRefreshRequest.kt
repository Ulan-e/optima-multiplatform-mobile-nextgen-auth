package kg.optima.mobile.auth.data.api.model.jwt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class JwtRefreshRequest(
    @SerialName("device_id")
    val deviceId: String,

    @SerialName("refresh_token")
    val refreshToken: String,
)