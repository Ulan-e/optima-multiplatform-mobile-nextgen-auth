package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.UiIntent
import kg.optima.mobile.registration.domain.usecase.GetQuestionsUseCase
import kg.optima.mobile.registration.presentation.control_question.model.Question
import org.koin.core.component.inject

class ControlQuestionIntent(
	override val uiState: ControlQuestionState,
) : UiIntent<ControlQuestionInfo>() {

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
					questions = list
				)
			}
		}
	}

	fun onValueChanged(number: String) {
		answerValidator.validate(number).fold(
			fnL = { uiState.handle(ControlQuestionInfo.Validation(false, it.message)) },
			fnR = { uiState.handle(ControlQuestionInfo.Validation(true)) }
		)
	}

	fun showQuestions() {
		uiState.handle(ControlQuestionInfo.ShowQuestions)
	}

	fun hideQuestions() {
		uiState.handle(ControlQuestionInfo.HideQuestions)
	}

	fun setQuestion(question: Question) {
		uiState.handle(ControlQuestionInfo.SetQuestion(question))
	}

	fun confirm(hashCode: String, questionId: String, answer: String) {
		uiState.handle(ControlQuestionInfo.ConfirmQuestion(hashCode, questionId, answer))
	}

}