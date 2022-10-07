package kg.optima.mobile.registration.domain.usecase

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.CheckPhoneEntity

class CheckPhoneNumberUseCase(
	private val registrationRepository: RegistrationRepository,
	private val registrationPreferences: RegistrationPreferences,
) : BaseUseCase<String, CheckPhoneEntity>() {

	override suspend fun execute(model: String): Either<Failure, CheckPhoneEntity> {
		return registrationRepository.checkPhoneNumber(model).map {
			CheckPhoneEntity(
				success = it.success,
				referenceId = it.data?.refId.orEmpty(),
				timeLeft = it.data?.timeLeft ?: 0L,
				message = it.message,
			)
		}.onSuccess {
			registrationPreferences.referenceId = it.referenceId
		}
	}

}