package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CodeCheckRequest(
    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("verificationCode")
    val verificationCode : String,
)