package kg.optima.mobile.registration.domain.model

data class OtpTriesEntity(
    val phoneNumber: String,
    val tryCount: Int,
    val tryTime: Long,
)