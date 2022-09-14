package kg.optima.mobile.registration.presentation.secret_question

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.feature.register.RegistrationScreenModel
import kg.optima.mobile.registration.presentation.secret_question.model.Question

class SecretQuestionState : State<SecretQuestionInfo>() {

	override fun handle(entity: SecretQuestionInfo) {
		val stateModel: StateModel = when (entity) {
			SecretQuestionInfo.ShowQuestions -> SecretQuestionModel.ShowQuestions
			SecretQuestionInfo.HideQuestions -> SecretQuestionModel.HideQuestions
			is SecretQuestionInfo.SetQuestion -> SecretQuestionModel.SetQuestion(entity.question)
			is SecretQuestionInfo.GetQuestions -> SecretQuestionModel.GetQuestions(entity.questions)
			is SecretQuestionInfo.Validation -> SecretQuestionModel.ValidateResult(entity.success, entity.message)
			is SecretQuestionInfo.ConfirmQuestion -> {
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

	sealed interface SecretQuestionModel : StateModel {

		object ShowQuestions : SecretQuestionModel

		object HideQuestions : SecretQuestionModel

		class SetQuestion(
			val question: Question
		) : SecretQuestionModel

		class GetQuestions(
			val questions: List<Question>
		) : SecretQuestionModel

		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : SecretQuestionModel

	}

}