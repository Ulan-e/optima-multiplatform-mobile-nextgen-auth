package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.CheckPhoneEntity

class CheckPhoneNumberUseCase(
	private val registrationRepository: RegistrationRepository,
) : BaseUseCase<String, CheckPhoneEntity>() {

	override suspend fun execute(model: String): Either<Failure, CheckPhoneEntity> {
		return registrationRepository.checkPhoneNumber(model).map {
			CheckPhoneEntity(
				success = it.isSuccess,
				referenceId = it.data?.refId.orEmpty(),
			)
		}
	}

}