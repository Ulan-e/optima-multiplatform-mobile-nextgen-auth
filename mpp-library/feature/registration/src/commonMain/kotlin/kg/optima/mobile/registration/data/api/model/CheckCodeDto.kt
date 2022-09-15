package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CheckCodeDto(
    @SerialName("date")
    val date: String?,

    @SerialName("access_token")
    val accessToken: String?,

    @SerialName("person_id")
    val personId: String?
)