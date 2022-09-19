package kg.optima.mobile.registration.data.model.otp_tries

import kotlinx.serialization.Serializable

@Serializable
class OtpTryModel(
    val tryCount: Int,
    val tryTime: Long,
) {
    companion object {
        val NO_TRIES = OtpTryModel(
            tryCount = 0,
            tryTime = 0
        )
    }
}