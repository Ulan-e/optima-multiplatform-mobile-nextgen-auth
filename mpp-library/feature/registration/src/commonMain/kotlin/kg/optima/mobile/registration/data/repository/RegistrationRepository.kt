package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.model.CheckCodeDto
import kg.optima.mobile.registration.data.api.model.VerifyClientRequest
import kg.optima.mobile.registration.data.api.model.PhoneCheckDto
import kg.optima.mobile.registration.data.api.model.VerifyClientDto


interface RegistrationRepository {
    suspend fun checkPhoneNumber(phoneNumber: String): Either<Failure, BaseDto<PhoneCheckDto>>
    suspend fun checkSmsCode(
        phoneNumber: String,
        smsCode: String,
        referenceId: String,
    ): Either<Failure, BaseDto<CheckCodeDto>>

    suspend fun verifyClient(
        referenceId: String,
        sessionId: String,
        livenessResult: String,
        accessToken: String,
        personId: String,
        documentData: VerifyClientRequest
    ): Either<Failure, BaseDto<VerifyClientDto>>
}