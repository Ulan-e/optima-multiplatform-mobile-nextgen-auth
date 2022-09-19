package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.model.CheckCodeDto
import kg.optima.mobile.registration.data.api.registration.RegistrationApi
import kg.optima.mobile.registration.data.api.model.CodeCheckRequest
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest
import kg.optima.mobile.registration.data.api.model.VerifyClientDto
import kg.optima.mobile.registration.data.api.model.VerifyClientRequest
import kg.optima.mobile.registration.data.api.model.RegistrationRequest
import kg.optima.mobile.registration.data.api.verification.VerificationApi

class RegistrationRepositoryImpl(
    private val registrationApi: RegistrationApi,
    private val verificationApi: VerificationApi
) : RegistrationRepository, BaseDataSource() {

    override suspend fun checkPhoneNumber(phoneNumber: String) = apiCall {
        verificationApi.checkPhoneNumber(PhoneCheckRequest(phoneNumber))
    }

    override suspend fun checkSmsCode(
        phoneNumber: String,
        smsCode: String, referenceId: String
    ): Either<Failure, BaseDto<CheckCodeDto>> = apiCall {
        verificationApi.checkSmsCode(CodeCheckRequest(phoneNumber, smsCode), referenceId)
    }

	override suspend fun verifyClient(
		referenceId: String,
		sessionId: String,
		livenessResult: String,
		accessToken: String,
		personId: String,
		documentData: VerifyClientRequest
	): Either<Failure, BaseDto<VerifyClientDto>> = apiCall {
		verificationApi.verifyClient(
			referenceId,
			sessionId,
			livenessResult,
			accessToken,
			personId,
			documentData
		)
	}

    override suspend fun getQuestions() = apiCall {
        registrationApi.getQuestions()
    }

    override suspend fun register(
        hash: String,
        hashPassword: String,
        questionId: String,
        answer: String
    ): Either<Failure, BaseDto<String>> = apiCall {
        registrationApi.register(RegistrationRequest(hash, hashPassword, questionId, answer))
    }

}