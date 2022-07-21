package kg.optima.mobile.network.failure

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val system: String?,
    val status: Int?,
    val message: String?,
    val developerMessage: String?,
)