package kg.optima.mobile.registration.domain.model

class CheckPhoneEntity(
    val success: Boolean,
    val referenceId: String,
    val timeLeft : Long
)