package kg.optima.mobile.auth.domain.usecase.pin_set

import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

// TODO check pin on server
class PinSetUseCase(
	private val authRepository: AuthRepository,
	private val component: FeatureAuthComponent,
) : BaseUseCase<PinSetUseCase.Params, PinSetResult>() {

	override suspend fun execute(
		model: Params,
	): Either<Failure, PinSetResult> {
		return when (model) {
			is Params.Save -> {
				component.pin = model.pin; Either.Right(PinSetResult.Save)
			}
			is Params.Compare ->
				Either.Right(PinSetResult.Compare(component.pin == model.pin))
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