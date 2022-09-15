package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.feature.register.RegistrationScreenModel
import kg.optima.mobile.registration.presentation.control_question.model.Question

class ControlQuestionState : State<ControlQuestionInfo>() {

	override fun handle(entity: ControlQuestionInfo) {
		val stateModel: StateModel = when (entity) {
			ControlQuestionInfo.ShowQuestions -> ControlQuestionModel.ShowQuestions
			ControlQuestionInfo.HideQuestions -> ControlQuestionModel.HideQuestions
			is ControlQuestionInfo.SetQuestion -> ControlQuestionModel.SetQuestion(entity.question)
			is ControlQuestionInfo.GetQuestions -> ControlQuestionModel.GetQuestions(entity.questions)
			is ControlQuestionInfo.Validation -> ControlQuestionModel.ValidateResult(entity.success, entity.message)
			is ControlQuestionInfo.ConfirmQuestion -> {
				val screenModel = RegistrationScreenModel.Password(
					hashCode = entity.hashCode,
					questionId = entity.questionId,
					answer = entity.answer
				)
				StateModel.Navigate(screenModel)
			}
		}
		setStateModel(stateModel)
	}

	sealed interface ControlQuestionModel : StateModel {

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

	}

}