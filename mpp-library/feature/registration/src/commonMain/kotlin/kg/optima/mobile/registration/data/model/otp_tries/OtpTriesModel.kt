package kg.optima.mobile.registration.data.model.otp_tries

import kotlinx.serialization.Serializable

@Serializable
class OtpTriesModel(
	val phoneNumber: String,
	val tryCount: Int,
	val tryTime: Long,
)

@Serializable
class OtpTriesModelList(
	val list: List<OtpTriesModel>
) {
	companion object {
		val NO_TRIES = OtpTriesModelList(
			list = listOf()
		)
	}
}