package kg.optima.mobile.registration.domain.model

class CheckCodeEntity(
    val success: Boolean,
    val date: String,
    val accessToken: String,
    val personId: String
)