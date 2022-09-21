package kg.optima.mobile.registration.domain.model

class RegisterClientEntity(
    val code: Int,
    val success: Boolean,
    val message: String,
    val clientId: String?
)