package kg.optima.mobile.registration.domain.usecase

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository

class RegistrationUseCase(
	private val repository: RegistrationRepository
) : BaseUseCase<RegistrationUseCase.Params, Boolean>() {

	override suspend fun execute(model: Params): Either<Failure, Boolean> {
		return repository.register(model.hash, model.hashPassword, model.questionId, model.answer)
			.map { true }
	}

	class Params(
		val hash: String,
		val hashPassword: String,
		val questionId: String,
		val answer: String,
	)
}