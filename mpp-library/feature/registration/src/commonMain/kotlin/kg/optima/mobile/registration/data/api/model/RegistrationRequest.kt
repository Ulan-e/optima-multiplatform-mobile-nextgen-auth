package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RegistrationRequest(
	@SerialName("Hash")
	val hash: String,
	@SerialName("HashPassword")
	val hashPassword: String,
	@SerialName("QuestionId")
	val questionId: String,
	@SerialName("Answer")
	val answer: String
)