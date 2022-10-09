package kg.optima.mobile.registration.presentation.control_question

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel
import kg.optima.mobile.registration.presentation.control_question.model.Question

class ControlQuestionState : UiState<ControlQuestionInfo>() {

	override fun handle(entity: ControlQuestionInfo) {
		val stateModel: UiState.Model = when (entity) {
			ControlQuestionInfo.ShowQuestions -> Model.ShowQuestions
			ControlQuestionInfo.HideQuestions -> Model.HideQuestions
			is ControlQuestionInfo.SetQuestion -> Model.SetQuestion(entity.question)
			is ControlQuestionInfo.GetQuestions -> Model.GetQuestions(entity.questions)
			is ControlQuestionInfo.Validation -> Model.ValidateResult(
				entity.success,
				entity.message
			)
			is ControlQuestionInfo.ConfirmQuestion ->
				Model.NavigateTo.CreatePassword(
					hash = entity.hashCode,
					questionId = entity.questionId,
					answer = entity.answer
				)
		}
		setStateModel(stateModel)
	}

	sealed interface Model : UiState.Model {
		object ShowQuestions :
			Model
		object HideQuestions :
			Model

		class SetQuestion(
			val question: Question
		) : Model

		class GetQuestions(
			val questions: List<Question>
		) : Model

		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : Model

		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			class CreatePassword(
				val hash: String,
				val questionId: String,
				val answer: String,
			) : NavigateTo, RegistrationNavigateModel
		}
	}

}