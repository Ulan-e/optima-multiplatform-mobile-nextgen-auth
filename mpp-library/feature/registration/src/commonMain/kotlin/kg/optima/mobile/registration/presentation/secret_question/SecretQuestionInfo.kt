package kg.optima.mobile.registration.presentation.secret_question

import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.registration.presentation.secret_question.model.Question

sealed interface SecretQuestionInfo {

	class GetQuestions(
		val success: Boolean,
		val questions : List<Question>
	) : SecretQuestionInfo

	class SetQuestion(
		val question : Question
	) : SecretQuestionInfo

	class Validation(
		val success: Boolean,
		val message: String = emptyString,
	) : SecretQuestionInfo

	class ConfirmQuestion(
		val hashCode: String,
		val questionId: String,
		val answer: String
	) : SecretQuestionInfo


	object ShowQuestions: SecretQuestionInfo

	object HideQuestions: SecretQuestionInfo

}