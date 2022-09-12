package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository

class CheckPhoneNumberUseCase(
	private val registrationRepository: RegistrationRepository,
) : BaseUseCase<String, Boolean>() {

	override suspend fun execute(model: String): Either<Failure, Boolean> {
		return registrationRepository.checkPhoneNumber(model).map { it.isSuccess }
	}

}