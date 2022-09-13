package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository

class CheckSmsCodeUseCase(
    private val registrationRepository: RegistrationRepository,
) : BaseUseCase<CheckSmsCodeUseCase.Params, Boolean>() {

    override suspend fun execute(model: Params): Either<Failure, Boolean> {
        return registrationRepository.checkSmsCode(model.phoneNumber, model.verificationCode)
            .map { it.isSuccess }
    }

    class Params(
        val phoneNumber: String,
        val verificationCode: String,
    )

}