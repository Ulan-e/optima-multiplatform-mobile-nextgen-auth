package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneCheckDto(
    @SerialName("refId")
    val refId: String?,

    @SerialName("count")
    val count: Int?,

    @SerialName("date")
    val date: String?,

    @SerialName("ms")
    val timeLeft: Long?,
)