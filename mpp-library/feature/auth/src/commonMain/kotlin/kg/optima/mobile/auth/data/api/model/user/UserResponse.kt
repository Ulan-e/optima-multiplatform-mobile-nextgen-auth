package kg.optima.mobile.auth.data.api.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponse(
    @SerialName("base_profile_id")
    val baseProfileId: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("is_banned")
    val isBanned: Boolean? = null,

    @SerialName("is_super_admin")
    val isSuperAdmin: Boolean? = null,

    @SerialName("is_verified")
    val isVerified: Boolean? = null,

    @SerialName("mobile")
    val mobile: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,
)