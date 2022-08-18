package kg.optima.mobile.auth.domain.usecase.pin_set

import kg.optima.mobile.auth.data.component.AuthPreferences
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

// TODO check pin on server
class PinSetUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<PinSetUseCase.Params, PinSetResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, PinSetResult> {
		return when (model) {
			is Params.Save -> {
				authPreferences.pin = model.pin; Either.Right(PinSetResult.Save)
			}
			is Params.Compare ->
				Either.Right(PinSetResult.Compare(authPreferences.pin == model.pin))
		}
	}

	sealed interface Params {
		class Save(
			val pin: String,
		) : Params

		class Compare(
			val pin: String,
		) : Params
	}
}