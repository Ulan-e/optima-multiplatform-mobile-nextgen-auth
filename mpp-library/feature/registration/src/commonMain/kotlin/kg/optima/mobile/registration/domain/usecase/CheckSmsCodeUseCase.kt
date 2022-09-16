package kg.optima.mobile.registration.domain.usecase

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.CheckCodeEntity

class CheckSmsCodeUseCase(
    private val registrationRepository: RegistrationRepository,
    private val registrationPreferences: RegistrationPreferences
) : BaseUseCase<CheckSmsCodeUseCase.Params, CheckCodeEntity>() {

    override suspend fun execute(model: Params): Either<Failure, CheckCodeEntity> {
        return registrationRepository.checkSmsCode(
            phoneNumber = model.phoneNumber,
            smsCode = model.verificationCode,
            referenceId = model.referenceId
        ).map {
            CheckCodeEntity(
                success = true,
                date = it.data?.date.orEmpty(),
                accessToken = it.data?.accessToken.orEmpty(),
                personId = it.data?.personId.orEmpty()
            )
        }.onSuccess {
            registrationPreferences.accessToken = it.accessToken
            registrationPreferences.personId = it.personId
        }
    }

    class Params(
        val phoneNumber: String,
        val verificationCode: String,
        val referenceId: String,
    )

}