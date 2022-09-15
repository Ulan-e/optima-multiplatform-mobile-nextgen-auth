package kg.optima.mobile.registration.presentation.control_question

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.core.error.Failure

object AnswerValidator {

	fun validate(answer: String): Either<Failure, Unit> {
		return when {
			answer.length >= Constants.CONTROL_ANSWER_LENGTH ->
				Either.Right(Unit)
			else ->
				Either.Left(Failure.Message("Должен содержать не менее ${Constants.CONTROL_ANSWER_LENGTH} символов!))"))
		}
	}

}