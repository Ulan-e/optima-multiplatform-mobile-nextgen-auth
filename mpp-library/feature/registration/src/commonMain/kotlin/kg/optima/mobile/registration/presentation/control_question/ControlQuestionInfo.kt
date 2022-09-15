package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.registration.presentation.control_question.model.Question

sealed interface ControlQuestionInfo {

	class GetQuestions(
		val success: Boolean,
		val questions : List<Question>
	) : ControlQuestionInfo

	class SetQuestion(
		val question : Question
	) : ControlQuestionInfo

	class Validation(
		val success: Boolean,
		val message: String = emptyString,
	) : ControlQuestionInfo

	class ConfirmQuestion(
		val hashCode: String,
		val questionId: String,
		val answer: String
	) : ControlQuestionInfo


	object ShowQuestions: ControlQuestionInfo

	object HideQuestions: ControlQuestionInfo

}