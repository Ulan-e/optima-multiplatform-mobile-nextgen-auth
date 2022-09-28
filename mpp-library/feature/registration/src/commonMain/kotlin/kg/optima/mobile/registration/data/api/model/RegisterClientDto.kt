package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterClientDto(
    @SerialName("client_id")
    val clientId: String?,
    @SerialName("phone_number")
    val phoneNumber: String?,
    @SerialName("verification_passed")
    val passed: Boolean?
)