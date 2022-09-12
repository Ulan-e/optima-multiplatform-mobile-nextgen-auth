package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository

class ReRequestSmsCodeUseCase(
    private val registrationRepository: RegistrationRepository,
) : BaseUseCase<Unit, Int>() {

    override suspend fun execute(model: Unit): Either<Failure, Int> {
        return registrationRepository.reRequestSmsCode().map { it }
    }


}