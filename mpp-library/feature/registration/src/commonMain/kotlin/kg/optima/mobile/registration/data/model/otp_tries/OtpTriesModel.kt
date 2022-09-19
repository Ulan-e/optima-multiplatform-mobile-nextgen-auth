package kg.optima.mobile.registration.data.model.otp_tries

import kg.optima.mobile.base.utils.emptyString
import kotlinx.serialization.Serializable

@Serializable
class OtpTriesModel(
    val phoneNumber: String,
    val tryCount: Int,
    val tryTime: Long,
) {
    companion object {
        val NO_TRIES = OtpTriesModel(
            phoneNumber = emptyString,
            tryCount = 0,
            tryTime = 0
        )
    }
}