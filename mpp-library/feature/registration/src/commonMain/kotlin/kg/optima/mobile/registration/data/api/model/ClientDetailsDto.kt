package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ClientDetailsDto(
    @SerialName("clientDetails")
    val clientDetailsDto: RegisterClientDto?
)