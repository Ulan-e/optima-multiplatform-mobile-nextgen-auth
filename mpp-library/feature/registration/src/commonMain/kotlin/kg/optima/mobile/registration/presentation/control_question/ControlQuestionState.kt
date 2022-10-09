package kg.optima.mobile.registration.presentation.control_question

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.registration.presentation.control_question.model.Question

class ControlQuestionState : UiState<ControlQuestionInfo>() {

	override fun handle(entity: ControlQuestionInfo) {
		val stateModel: Model = when (entity) {
			ControlQuestionInfo.ShowQuestions -> ControlQuestionModel.ShowQuestions
			ControlQuestionInfo.HideQuestions -> ControlQuestionModel.HideQuestions
			is ControlQuestionInfo.SetQuestion -> ControlQuestionModel.SetQuestion(entity.question)
			is ControlQuestionInfo.GetQuestions -> ControlQuestionModel.GetQuestions(entity.questions)
			is ControlQuestionInfo.Validation -> ControlQuestionModel.ValidateResult(
				entity.success,
				entity.message
			)
			is ControlQuestionInfo.ConfirmQuestion ->
				ControlQuestionModel.NavigateToCreatePassword(
					hash = entity.hashCode,
					questionId = entity.questionId,
					answer = entity.answer
				)
		}
		setStateModel(stateModel)
	}

	sealed interface ControlQuestionModel : Model {
		object ShowQuestions : ControlQuestionModel
		object HideQuestions : ControlQuestionModel

		class SetQuestion(
			val question: Question
		) : ControlQuestionModel

		class GetQuestions(
			val questions: List<Question>
		) : ControlQuestionModel

		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : ControlQuestionModel

		@Parcelize
		class NavigateToCreatePassword(
			val hash: String,
			val questionId: String,
			val answer: String,
		) : ControlQuestionModel, Model.Navigate
	}

}