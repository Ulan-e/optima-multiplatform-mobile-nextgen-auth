package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.RegistrationApi
import kg.optima.mobile.registration.data.api.model.CodeCheckRequest
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest
import kg.optima.mobile.registration.data.api.model.RegistrationRequest


class RegistrationRepositoryImpl(
	private val registrationApi: RegistrationApi,
) : RegistrationRepository, BaseDataSource() {
	override suspend fun checkPhoneNumber(phoneNumber: String) = apiCall {
		registrationApi.checkPhoneNumber(PhoneCheckRequest(phoneNumber))
	}

	override suspend fun checkSmsCode(phoneNumber: String, smsCode: String, referenceId: String) = apiCall {
		registrationApi.checkSmsCode(CodeCheckRequest(phoneNumber, smsCode), referenceId)
	}

	override suspend fun register(
		hash: String,
		hashPassword: String,
		questionId: String,
		answer: String
	): Either<Failure, BaseDto<String>> = apiCall{
		registrationApi.register(RegistrationRequest(hash, hashPassword, questionId, answer))
	}

}