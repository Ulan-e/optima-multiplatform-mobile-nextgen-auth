package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.domain.model.OtpTriesEntity
import kg.optima.mobile.registration.domain.model.utils.toOtpTriesEntity

class GetTriesDataUseCase(
    private val registrationPreferences: RegistrationPreferences
) : BaseUseCase<GetTriesDataUseCase.Params, List<OtpTriesEntity>>() {

    override suspend fun execute(model: Params): Either<Failure, List<OtpTriesEntity>> =
        Either.Right(
            registrationPreferences.otpTriesModelList.toOtpTriesEntity()
        )

    object Params

}