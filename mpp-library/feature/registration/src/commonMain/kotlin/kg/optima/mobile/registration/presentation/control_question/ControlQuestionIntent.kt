package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.GetQuestionsUseCase
import kg.optima.mobile.registration.presentation.control_question.model.Question
import org.koin.core.component.inject

class ControlQuestionIntent(
	override val state: ControlQuestionState,
) : Intent<ControlQuestionInfo>() {

	private val getQuestionsUseCase: GetQuestionsUseCase by inject()
	private val answerValidator = AnswerValidator

	fun getQuestions() {
		launchOperation {
			getQuestionsUseCase.execute(Unit).map {
				val list = mutableListOf<Question>()
				it.list.map { entity ->
					list.add(
						Question(
							question = entity.question,
							questionId = entity.questionId
						)
					)
				}
				ControlQuestionInfo.GetQuestions(
					success = it.success,
					questions = list)
			}
		}
	}

	fun onValueChanged(number: String) {
		answerValidator.validate(number).fold(
			fnL = { state.handle(ControlQuestionInfo.Validation(false, it.message)) },
			fnR = { state.handle(ControlQuestionInfo.Validation(true)) }
		)
	}

	fun showQuestions() {
		state.handle(ControlQuestionInfo.ShowQuestions)
	}

	fun hideQuestions() {
		state.handle(ControlQuestionInfo.HideQuestions)
	}

	fun setQuestion(question: Question) {
		state.handle(ControlQuestionInfo.SetQuestion(question))
	}

	fun confirm(hashCode: String, questionId: String, answer: String) {
		state.handle(ControlQuestionInfo.ConfirmQuestion(hashCode, questionId, answer))
	}

}