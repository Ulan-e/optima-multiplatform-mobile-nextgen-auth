package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure

class CheckPhoneNumberUseCase : BaseUseCase<String, Boolean>() {
	override suspend fun execute(model: String): Either<Failure, Boolean> {
		return Either.Right(true)
	}
}