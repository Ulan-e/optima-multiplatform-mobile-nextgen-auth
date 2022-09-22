package kg.optima.mobile.registration.domain.usecase

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.model.VerifyClientRequest
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.domain.model.VerifyClientEntity

class VerifyClientUseCase(
    private val registrationRepository: RegistrationRepository,
    private val registrationPreferences: RegistrationPreferences
) : BaseUseCase<VerifyClientUseCase.Params, VerifyClientEntity>() {

    override suspend fun execute(model: Params): Either<Failure, VerifyClientEntity> {
        return registrationRepository.verifyClient(
            referenceId = registrationPreferences.referenceId!!,
            sessionId = model.sessionId,
            livenessResult = model.livenessResult,
            accessToken = registrationPreferences.accessToken!!,
            personId = registrationPreferences.personId!!,
            documentData = VerifyClientRequest(model.data)
        ).map { response ->
            VerifyClientEntity(
                success = response.success,
                hash = response.data?.hash,
                message = response.message ?: ""
            )
        }.onSuccess {
            registrationPreferences.hash = it.hash
        }
    }

    class Params(
        val sessionId: String,
        val livenessResult: String,
        val data: Map<String, String>
    )
}