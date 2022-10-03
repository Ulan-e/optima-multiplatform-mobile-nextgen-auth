package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.BaseMppIntent
import kg.optima.mobile.registration.domain.usecase.GetQuestionsUseCase
import kg.optima.mobile.registration.presentation.control_question.model.Question
import org.koin.core.component.inject

class ControlQuestionIntent(
	override val mppState: ControlQuestionState,
) : BaseMppIntent<ControlQuestionInfo>() {

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
			fnL = { mppState.handle(ControlQuestionInfo.Validation(false, it.message)) },
			fnR = { mppState.handle(ControlQuestionInfo.Validation(true)) }
		)
	}

	fun showQuestions() {
		mppState.handle(ControlQuestionInfo.ShowQuestions)
	}

	fun hideQuestions() {
		mppState.handle(ControlQuestionInfo.HideQuestions)
	}

	fun setQuestion(question: Question) {
		mppState.handle(ControlQuestionInfo.SetQuestion(question))
	}

	fun confirm(hashCode: String, questionId: String, answer: String) {
		mppState.handle(ControlQuestionInfo.ConfirmQuestion(hashCode, questionId, answer))
	}

}