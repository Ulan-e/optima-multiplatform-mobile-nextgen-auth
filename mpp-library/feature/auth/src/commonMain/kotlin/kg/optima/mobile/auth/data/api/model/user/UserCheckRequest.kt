package kg.optima.mobile.auth.data.api.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserCheckRequest(
    @SerialName("mobile")
    val mobile: String
)