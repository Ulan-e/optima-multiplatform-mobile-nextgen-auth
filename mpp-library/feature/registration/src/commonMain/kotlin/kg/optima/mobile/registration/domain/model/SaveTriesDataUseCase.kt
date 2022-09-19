package kg.optima.mobile.registration.domain.model

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModel
import kg.optima.mobile.registration.presentation.sms_code.CheckSmsCodeInfo

class SaveTriesDataUseCase(
    private val registrationPreferences: RegistrationPreferences
) : BaseUseCase<SaveTriesDataUseCase.Params, CheckSmsCodeInfo>() {

    override suspend fun execute(model: Params): Either<Failure, CheckSmsCodeInfo> {
        registrationPreferences.otpTriesModel = OtpTriesModel(
            phoneNumber = model.phoneNumber,
            tryCount = model.tryCount,
            tryTime = model.tryTime
        )
        return Either.Right(
            CheckSmsCodeInfo.TryDataSaved
        )
    }

    class Params(
        val phoneNumber: String,
        val tryCount: Int,
        val tryTime: Long,
    )

}