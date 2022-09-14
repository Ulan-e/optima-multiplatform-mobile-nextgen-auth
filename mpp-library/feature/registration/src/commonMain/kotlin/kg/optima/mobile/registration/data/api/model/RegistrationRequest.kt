package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.Serializable

@Serializable
class RegistrationRequest(
	val hash: String,
	val hashPassword: String,
	val questionId: String,
	val answer: String
)