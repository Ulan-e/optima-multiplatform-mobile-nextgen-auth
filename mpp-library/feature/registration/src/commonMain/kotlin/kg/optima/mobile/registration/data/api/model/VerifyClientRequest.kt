package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyClientRequest(
    @SerialName("data")
    val data: Map<String, String>
)