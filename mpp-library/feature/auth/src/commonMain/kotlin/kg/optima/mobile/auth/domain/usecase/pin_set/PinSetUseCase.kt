package kg.optima.mobile.auth.domain.usecase.pin_set

import kg.optima.mobile.feature.auth.component.AuthPreferences
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

// TODO check pin on server
class PinSetUseCase(
	private val authPreferences: AuthPreferences,
) : BaseUseCase<PinSetUseCase.Params, SetupAuthResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, SetupAuthResult> {
		return when (model) {
			is Params.Save -> {
				authPreferences.pin = model.pin; Either.Right(SetupAuthResult.Save)
			}
			is Params.Compare -> {
				Either.Right(SetupAuthResult.Compare(isMatch = authPreferences.pin == model.pin))
			}
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