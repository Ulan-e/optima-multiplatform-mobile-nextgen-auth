package kg.optima.mobile.registration.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
	@SerialName("Question")
	val question: String,
	@SerialName("QuestionId")
	val questionId: String
)