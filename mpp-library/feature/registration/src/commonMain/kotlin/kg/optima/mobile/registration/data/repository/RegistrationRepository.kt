package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.model.*


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

	suspend fun getQuestions(): Either<Failure, BaseDto<List<QuestionDto>>>

	suspend fun register(
		hash: String,
		hashPassword: String,
		questionId: String,
		answer: String,
	): Either<Failure, BaseDto<RegisterClientDto>>
}