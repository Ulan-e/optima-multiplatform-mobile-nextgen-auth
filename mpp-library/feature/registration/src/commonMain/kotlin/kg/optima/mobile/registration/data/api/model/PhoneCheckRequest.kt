package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.Serializable

@Serializable
class PhoneCheckRequest(
	val phoneNumber: String,
)