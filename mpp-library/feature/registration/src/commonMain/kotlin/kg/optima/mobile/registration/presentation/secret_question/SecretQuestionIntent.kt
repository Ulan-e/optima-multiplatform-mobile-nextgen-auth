package kg.optima.mobile.registration.presentation.secret_question

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.GetQuestionsUseCase
import kg.optima.mobile.registration.presentation.secret_question.model.Question
import org.koin.core.component.inject

class SecretQuestionIntent(
	override val state: SecretQuestionState,
) : Intent<SecretQuestionInfo>() {

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
				SecretQuestionInfo.GetQuestions(
					success = it.success,
					questions = list)
			}
		}
	}

	fun onValueChanged(number: String) {
		answerValidator.validate(number).fold(
			fnL = { state.handle(SecretQuestionInfo.Validation(false, it.message)) },
			fnR = { state.handle(SecretQuestionInfo.Validation(true)) }
		)
	}

	fun showQuestions() {
		state.handle(SecretQuestionInfo.ShowQuestions)
	}

	fun hideQuestions() {
		state.handle(SecretQuestionInfo.HideQuestions)
	}

	fun setQuestion(question: Question) {
		state.handle(SecretQuestionInfo.SetQuestion(question))
	}

	fun confirm(hashCode: String, questionId: String, answer: String) {
		state.handle(SecretQuestionInfo.ConfirmQuestion(hashCode, questionId, answer))
	}

}