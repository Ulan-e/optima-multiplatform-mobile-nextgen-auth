package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.CheckCodeEntity

class CheckSmsCodeUseCase(
    private val registrationRepository: RegistrationRepository,
) : BaseUseCase<CheckSmsCodeUseCase.Params, CheckCodeEntity>() {

    override suspend fun execute(model: Params): Either<Failure, CheckCodeEntity> {
        return registrationRepository.checkSmsCode(
            phoneNumber = model.phoneNumber,
            smsCode = model.verificationCode,
            referenceId = model.referenceId
        ).map {
            CheckCodeEntity(
                success = it.isSuccess,
                date = it.data?.date.orEmpty(),
                accessToken = it.data?.accessToken.orEmpty(),
                personId = it.data?.personId.orEmpty()
            )
        }
    }

    class Params(
        val phoneNumber: String,
        val verificationCode: String,
        val referenceId: String,
    )

}