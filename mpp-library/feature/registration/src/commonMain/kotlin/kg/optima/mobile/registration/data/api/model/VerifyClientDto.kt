package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class VerifyClientDto(
    @SerialName("hash")
    val hash: String?
)