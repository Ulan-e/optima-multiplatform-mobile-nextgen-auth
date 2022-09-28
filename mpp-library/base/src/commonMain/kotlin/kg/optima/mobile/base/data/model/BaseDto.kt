package kg.optima.mobile.base.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BaseDto<T>(
    @SerialName("code")
    val code: Int,

    @SerialName("data")
    val data: T?,

    @SerialName("success")
    val success: Boolean = false,

    @SerialName("message")
    val message: String?,
)