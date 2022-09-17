package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName

class VerifyClientDto(
    @SerialName("hash")
    val hash: String?
)